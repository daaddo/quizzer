package it.cascella.quizzer.service;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.cascella.quizzer.controller.QuizController;
import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.*;
import it.cascella.quizzer.repository.*;
import it.cascella.quizzer.exceptions.QuizzerException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
        while (issuedQuizRepository.getByTokenId(token).isPresent()){
            token = tokenGenerator.generateToken(32);
        }
        issuedQuizRepository.insertIssuedQuiz(token,details.getId(),quizId,expirationDate,duration,numbOfQuestions);
        return String.format(token);
    }

    public List<GetQuestionDtoNotCorrected> getQuestionFromToken(String token, CustomUserDetails principal) throws QuizzerException {

        IssuedQuiz quizInformations = issuedQuizRepository.getByTokenId(token).orElseThrow(() -> new QuizzerException("Invalid or expired token", HttpStatus.BAD_REQUEST.value()));
        if(userQuizAttemptRepository.getByTokenAndUser_Id(token, principal.getId()).isPresent()){
            throw new QuizzerException("User has already requested the quiz", HttpStatus.FORBIDDEN.value());
        }
        userQuizAttemptRepository.insertIssuedQuiz(principal.getId(), token);


        List<Question> questions = questionRepository.findRandomQuestions(quizInformations.getNumberOfQuestions(),quizInformations.getQuiz().getId(),quizInformations.getIssuer().getId());

        return questions.stream()
                .map(question -> new GetQuestionDtoNotCorrected(
                        question.getId(),
                        question.getTitle(),
                        question.getQuestion(),
                        question.getAnswers().stream()
                                .map(answer -> new GetAnswerDtoNotCorrected(answer.getId(), answer.getAnswer() ))
                                .toList(),
                        question.getMultipleChoice()  )
                )
                .toList();
    }



    public HashMap<Integer, AnswerResponse> submitAnswers(String token, HashMap<Integer,AnswerResponse> answersByUser, CustomUserDetails principal) throws QuizzerException {
        QuizInformations quizInformations = cache.getIfPresent(token);
        if (quizInformations == null) {
            throw new QuizzerException("Invalid or expired token", HttpStatus.BAD_REQUEST.value());
        }
        if (!quizInformations.getUsersTakingTheQuiz().containsKey(principal) || quizInformations.getUsersTakingTheQuiz().get(principal).getStatus() != QuizUserInformation.Status.IN_PROGRESS) {
            throw new QuizzerException("User has not started the quiz or has already submitted it", HttpStatus.FORBIDDEN.value());
        }
        List<QuestionRepository.CorrectionAnswer> answersCorrection = questionRepository.getAnswersByQuizId(quizInformations.getQuizId());
        for (QuestionRepository.CorrectionAnswer answer : answersCorrection) {
            if (!answer.isCorrect()){
                continue;
            }
            answersByUser.get(answer.questionId()).getCorrectOptions().add(answer.answerId());
        }
        return answersByUser;
    }
}
