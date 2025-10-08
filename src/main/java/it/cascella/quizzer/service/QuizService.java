package it.cascella.quizzer.service;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.*;
import it.cascella.quizzer.repository.*;
import it.cascella.quizzer.exceptions.QuizzerException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final QuestionRepository questionRepository;
    private final IssuedQuizRepository issuedQuizRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, TokenGenerator tokenGenerator, QuestionRepository questionRepository, IssuedQuizRepository issuedQuizRepository, UserQuizAttemptRepository userQuizAttemptRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.questionRepository = questionRepository;
        this.issuedQuizRepository = issuedQuizRepository;
        this.userQuizAttemptRepository = userQuizAttemptRepository;
    }

    @Transactional
    @Modifying
    public void deleteQuiz(Integer id, Integer userId) {
        Integer i = quizRepository.deleteQuizByIdAndUserId_Id(id, userId);
        if (i != 1) {
            throw new RuntimeException("Quiz not found with id: " + id + " for user: " + userId);
        }
    }
    @Transactional
    @Modifying
    public Integer insertQuiz(NewQuizDTO newQuiz, Integer id) {
        Quiz quiz = new Quiz();
        quiz.setDescription( newQuiz.description());
        quiz.setTitle(newQuiz.title());

        quiz.setUserId(userRepository.getUsersById(id));
        quizRepository.save(quiz);
        return quiz.getId();
    }

    @Transactional
    @Modifying
    public void updateQuiz(PutQuizDTO newQuiz, Integer id) {

        Integer i = quizRepository.updateQuiz(newQuiz.title(), newQuiz.description(), newQuiz.id(), id);

        if (i != 1) {
            throw new RuntimeException("Quiz not found with id: " + newQuiz.id() + " for user: " + id);
        }
    }

    public String generateLink(
            Integer quizId,
            Integer numbOfQuestions,
            Time duration,
            LocalDateTime expirationDate,
            Boolean isAdditionalInfos,
            CustomUserDetails details
    ) throws QuizzerException {
        // Check if the quiz exists and belongs to the user
        quizRepository.findByIdAndUserId_Id(quizId, details.getId())
                .orElseThrow(() -> new QuizzerException("Quiz not found with id: " + quizId + " for user: " + details.getUsername(), HttpStatus.NOT_FOUND.value()));
        if (expirationDate!=null && expirationDate.isBefore(LocalDateTime.now())) {
            throw new QuizzerException("Expiration date must be in the future", HttpStatus.BAD_REQUEST.value());
        }
        if (duration.toLocalTime().isBefore(LocalTime.of(0,1))) {
            throw new QuizzerException("Duration must be at least 1 minute", HttpStatus.BAD_REQUEST.value());
        }
        if (expirationDate != null &&LocalDateTime.now().plusSeconds(duration.toLocalTime().toSecondOfDay()).isAfter(expirationDate.minus(Duration.ofMinutes(1)))){
            throw new QuizzerException("The duration is too long for the expiration date", HttpStatus.BAD_REQUEST.value());
        }
        String token = tokenGenerator.generateToken(32);
        log.info("Generated token: {}", token);
        while (issuedQuizRepository.getByTokenId(token).isPresent()){
            token = tokenGenerator.generateToken(32);
        }
        issuedQuizRepository.insertIssuedQuiz(
                token,
                details.getId(),
                quizId,
                expirationDate,
                duration,
                numbOfQuestions,
                isAdditionalInfos
        );
        return String.format(token);
    }

    public HashMap<QuizInfos,List<GetQuestionDtoNotCorrected>> getQuestionFromToken(String token, CustomUserDetails principal,AdditionalInfoDTO additionalInfoDTO) throws QuizzerException {

        IssuedQuiz quizInformations = issuedQuizRepository.getByTokenId(token).orElseThrow(() -> new QuizzerException("Invalid or expired token", HttpStatus.FORBIDDEN.value()));
        if(userQuizAttemptRepository.getByTokenAndUser_Id(token, principal.getId()).isPresent()){
            throw new QuizzerException("User has already requested the quiz", HttpStatus.FORBIDDEN.value());
        }
        // check if the quiz has expired
        if (quizInformations.getExpiresAt() != null && quizInformations.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new QuizzerException("Quiz expired", HttpStatus.FORBIDDEN.value());
        }
        // check if the quiz requires additional information and if the user hasn't provided it

        if(quizInformations.getRequiredDetails() &&(
                (
                        additionalInfoDTO == null ||
                                (additionalInfoDTO.surname()== null || additionalInfoDTO.surname().isBlank()) ||
                                (additionalInfoDTO.user_name()==null || additionalInfoDTO.user_name().isBlank())
                )
        )){
            throw new QuizzerException("Additional information required", HttpStatus.BAD_REQUEST.value());
        }
        if (additionalInfoDTO != null) {
            userQuizAttemptRepository.insertIssuedQuiz(
                    principal.getId(),
                    token,
                    additionalInfoDTO.user_name() == null ? null : additionalInfoDTO.user_name(),
                    additionalInfoDTO.surname()== null? null : additionalInfoDTO.surname(),
                    additionalInfoDTO.middleName()== null ? null : additionalInfoDTO.middleName()
            );
        }
        else{
            userQuizAttemptRepository.insertIssuedQuiz(
                    principal.getId(),
                    token,
                    null,
                    null,
                    null
            );
        }


        List<Question> questions = questionRepository.findRandomQuestions(quizInformations.getNumberOfQuestions(),quizInformations.getQuiz().getId(),quizInformations.getIssuer().getId());
        Object[] objects = issuedQuizRepository.getIssuedQuizInfosForUser(token).orElseThrow(() -> new QuizzerException("No quiz info found for token: " + token, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        log.info(objects[0].toString());
        QuizInfos quizInfos = new QuizInfos((Object[]) objects[0]);


        HashMap<QuizInfos,List<GetQuestionDtoNotCorrected>> map = new HashMap<>();

        map.put(quizInfos,questions.stream()
                .map(question -> new GetQuestionDtoNotCorrected(
                        question.getId(),
                        question.getTitle(),
                        question.getQuestion(),
                        question.getAnswers().stream()
                                .map(answer -> new GetAnswerDtoNotCorrected(answer.getId(), answer.getAnswer() ))
                                .toList(),
                        question.getMultipleChoice()  )
                )
                .toList());
        return map;
    }



    @Transactional
    @Modifying
    public HashMap<Integer, AnswerResponse> submitAnswers(String token, HashMap<Integer,AnswerResponse> answersByUser, CustomUserDetails principal) throws QuizzerException {
        //todo add protection to check if question_id belongs to the quiz

        log.info("Submitting answers for token: {}, answers: {}", token, answersByUser.toString());
        IssuedQuiz quizInformations = issuedQuizRepository.getByTokenId(token).orElseThrow(() -> new QuizzerException("Invalid token", HttpStatus.BAD_REQUEST.value()));

        Optional<UserQuizAttempt> byTokenAndUserId = userQuizAttemptRepository.getByTokenAndUser_Id(token, principal.getId());
        if (byTokenAndUserId.isEmpty()){
            throw new QuizzerException("User has not started the quiz", HttpStatus.FORBIDDEN.value());
        }
        if (byTokenAndUserId.get().getStatus().equals(ProgressStatus.COMPLETED)){
            throw new QuizzerException("User has already submitted the quiz", HttpStatus.FORBIDDEN.value());
        }
        // controlliamo se il tempo di consegna del quiz è nella durata del quiz se no lo settiamo a scaduto diamo 10 secondi in più per evitare problemi di latenza
        if (byTokenAndUserId.get().getAttemptedAt().plusSeconds(quizInformations.getDuration().toLocalTime().toSecondOfDay()).plusSeconds(10).isBefore(LocalDateTime.now())){
            byTokenAndUserId.get().setStatus(ProgressStatus.EXPIRED);
            userQuizAttemptRepository.save(byTokenAndUserId.get());
            throw new QuizzerException("Time is up! Quiz expired", HttpStatus.FORBIDDEN.value());
        }
        if (quizInformations.getExpiresAt() != null && quizInformations.getExpiresAt().isBefore(LocalDateTime.now())){
            byTokenAndUserId.get().setStatus(ProgressStatus.EXPIRED);
            userQuizAttemptRepository.save(byTokenAndUserId.get());
            throw new QuizzerException("Quiz expired", HttpStatus.FORBIDDEN.value());
        }

        //prendiamo le risposte  dal db
        List<QuestionRepository.CorrectionAnswer> answersCorrection = questionRepository.getAnswersByQuizId(quizInformations.getQuiz().getId());
        log.trace("Correction answers: {}", answersCorrection.toString());

        for (QuestionRepository.CorrectionAnswer answer : answersCorrection) {
            if (!answer.isCorrect()){
                continue;
            }
            if (answersByUser.containsKey(answer.questionId())) {
                AnswerResponse answered = answersByUser.get(answer.questionId());
                answered.getCorrectOptions().add(answer.answerId());
                answersByUser.put(answer.questionId(), answered);
            }
        }
        byTokenAndUserId.get().setStatus(ProgressStatus.COMPLETED);
        byTokenAndUserId.get().setFinishedAt(LocalDateTime.now());
        byTokenAndUserId.get().setQuestions(answersByUser);
        byTokenAndUserId.get().setScore(calculateScore(answersByUser));
        userQuizAttemptRepository.save(byTokenAndUserId.get());

        return answersByUser;
    }

    private @NotNull Integer calculateScore(HashMap<Integer, AnswerResponse> answersByUser) {
        int score = 0;
        for (Map.Entry<Integer, AnswerResponse> entry : answersByUser.entrySet()) {
            AnswerResponse answerResponse = entry.getValue();
            Set<Integer> userAnswers = new HashSet<>(answerResponse.getSelectedOptions());
            Set<Integer> correctAnswers = new HashSet<>(answerResponse.getCorrectOptions());
            if (userAnswers.equals(correctAnswers)) {
                score++;
            }
        }

        return score;
    }

    @Transactional
    @Modifying
    public void deleteUserAttempt(String token, Integer targetUserId, CustomUserDetails principal) throws QuizzerException {
        IssuedQuiz issuedQuiz = issuedQuizRepository.getByTokenId(token)
                .orElseThrow(() -> new QuizzerException("Invalid token", HttpStatus.BAD_REQUEST.value()));
        if (!issuedQuiz.getIssuer().getId().equals(principal.getId())) {
            throw new QuizzerException("Forbidden: not the issuer", HttpStatus.FORBIDDEN.value());
        }
        int affected = userQuizAttemptRepository.deleteByTokenAndUserId(token, targetUserId);
        if (affected == 0) {
            throw new QuizzerException("Attempt not found", HttpStatus.NOT_FOUND.value());
        }
    }

    @Transactional
    @Modifying
    public void updateIssuedQuizExpiration(String token, LocalDateTime expiration, CustomUserDetails principal) throws QuizzerException {
        IssuedQuiz issuedQuiz = issuedQuizRepository.getByTokenId(token)
                .orElseThrow(() -> new QuizzerException("Invalid token", HttpStatus.BAD_REQUEST.value()));
        if (!issuedQuiz.getIssuer().getId().equals(principal.getId())) {
            throw new QuizzerException("Forbidden: not the issuer", HttpStatus.FORBIDDEN.value());
        }
        if (expiration != null && expiration.isBefore(LocalDateTime.now().plusMinutes(1))) {
            throw new QuizzerException("Expiration date must be in the future", HttpStatus.BAD_REQUEST.value());
        }
        int affected = issuedQuizRepository.updateExpiration(token, expiration);
        if (affected == 0) {
            throw new QuizzerException("Issued quiz not found", HttpStatus.NOT_FOUND.value());
        }
    }

    @Transactional
    @Modifying
    public void updateIssuedQuizNumberOfQuestions(String token, Integer numberOfQuestions, CustomUserDetails principal) throws QuizzerException {
        if (numberOfQuestions == null || numberOfQuestions <= 0) {
            throw new QuizzerException("numberOfQuestions must be > 0", HttpStatus.BAD_REQUEST.value());
        }
        IssuedQuiz issuedQuiz = issuedQuizRepository.getByTokenId(token)
                .orElseThrow(() -> new QuizzerException("Invalid token", HttpStatus.BAD_REQUEST.value()));
        if (!issuedQuiz.getIssuer().getId().equals(principal.getId())) {
            throw new QuizzerException("Forbidden: not the issuer", HttpStatus.FORBIDDEN.value());
        }
        int affected = issuedQuizRepository.updateNumberOfQuestions(token, numberOfQuestions);
        if (affected == 0) {
            throw new QuizzerException("Issued quiz not found", HttpStatus.NOT_FOUND.value());
        }
    }

    @Transactional
    @Modifying
    public void deleteIssuedQuiz(String token, CustomUserDetails principal) throws QuizzerException {
        IssuedQuiz issuedQuiz = issuedQuizRepository.getByTokenId(token)
                .orElseThrow(() -> new QuizzerException("Invalid token", HttpStatus.BAD_REQUEST.value()));
        if (!issuedQuiz.getIssuer().getId().equals(principal.getId())) {
            throw new QuizzerException("Forbidden: not the issuer", HttpStatus.FORBIDDEN.value());
        }
        int affected = issuedQuizRepository.deleteByToken(token);
        if (affected == 0) {
            throw new QuizzerException("Issued quiz not found", HttpStatus.NOT_FOUND.value());
        }
    }

    public List<GetQuestionDto> getQuestionsForToken(String token, Map<Integer, AnswerResponse> questionsPayload, CustomUserDetails principal) throws QuizzerException {
        IssuedQuiz issuedQuiz = issuedQuizRepository.getByTokenId(token)
                .orElseThrow(() -> new QuizzerException("Invalid token", HttpStatus.BAD_REQUEST.value()));
        if (!issuedQuiz.getIssuer().getId().equals(principal.getId())) {
            throw new QuizzerException("Forbidden: not the issuer", HttpStatus.FORBIDDEN.value());
        }

        Integer quizId = issuedQuiz.getQuiz().getId();
        List<Integer> requestedQuestionIds = new ArrayList<>(questionsPayload.keySet());
        if (requestedQuestionIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Question> questions = questionRepository.findByIdsInForQuiz(requestedQuestionIds, quizId);
        if (questions.size() != requestedQuestionIds.size()) {
            throw new QuizzerException("Some questions do not belong to the quiz", HttpStatus.FORBIDDEN.value());
        }

        return questions.stream().map(q -> new GetQuestionDto(
                q.getId(),
                q.getTitle(),
                q.getQuestion(),
                q.getAnswers().stream()
                        .map(a -> new GetAnswerDto(a.getId(), a.getAnswer(), a.getCorrect()))
                        .toList()
        )).toList();
    }

    public Boolean doesRequireDetails(String token) throws QuizzerException {
        return issuedQuizRepository.isAdditionalInformationRequired(token).orElseThrow(()-> new QuizzerException("Invalid token", HttpStatus.BAD_REQUEST.value()));
    }
}
