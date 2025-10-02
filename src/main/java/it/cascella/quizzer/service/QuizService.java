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
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final Cache<String, QuizInformations> cache;
    private final TokenGenerator tokenGenerator;
    private final QuestionRepository questionRepository;
    private final IssuedQuizRepository issuedQuizRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, TokenGenerator tokenGenerator, QuestionRepository questionRepository, IssuedQuizRepository issuedQuizRepository, UserQuizAttemptRepository userQuizAttemptRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.questionRepository = questionRepository;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS) // TTL 15 minuti
                .build();
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

    public String generateLink(Integer quizId, Integer numbOfQuestions, Time duration, LocalDateTime expirationDate, CustomUserDetails details) throws QuizzerException {
        // Check if the quiz exists and belongs to the user
        quizRepository.findByIdAndUserId_Id(quizId, details.getId())
                .orElseThrow(() -> new QuizzerException("Quiz not found with id: " + quizId + " for user: " + details.getUsername(), HttpStatus.NOT_FOUND.value()));
        String token = tokenGenerator.generateToken(32);
        log.info("Generated token: {}", token);
        while (issuedQuizRepository.getByTokenId(token).isPresent()){
            token = tokenGenerator.generateToken(32);
        }
        issuedQuizRepository.insertIssuedQuiz(token,details.getId(),quizId,expirationDate,duration,numbOfQuestions);
        return String.format(token);
    }

    public HashMap<QuizInfos,List<GetQuestionDtoNotCorrected>> getQuestionFromToken(String token, CustomUserDetails principal) throws QuizzerException {

        IssuedQuiz quizInformations = issuedQuizRepository.getByTokenId(token).orElseThrow(() -> new QuizzerException("Invalid or expired token", HttpStatus.BAD_REQUEST.value()));
        if(userQuizAttemptRepository.getByTokenAndUser_Id(token, principal.getId()).isPresent()){
            throw new QuizzerException("User has already requested the quiz", HttpStatus.FORBIDDEN.value());
        }
        userQuizAttemptRepository.insertIssuedQuiz(principal.getId(), token);


        List<Question> questions = questionRepository.findRandomQuestions(quizInformations.getNumberOfQuestions(),quizInformations.getQuiz().getId(),quizInformations.getIssuer().getId());
        QuizInfos quizInfos = issuedQuizRepository.getIssuedQuizInfosForUser(token).orElseThrow(() -> new QuizzerException("No quiz info found for token: " + token, HttpStatus.INTERNAL_SERVER_ERROR.value()));
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
        byTokenAndUserId.get().setFinishedAt(Instant.now());
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
}
