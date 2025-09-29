package it.cascella.quizzer.service;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.cascella.quizzer.controller.QuizController;
import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.entities.Question;
import it.cascella.quizzer.entities.Quiz;
import it.cascella.quizzer.repository.QuizRepository;
import it.cascella.quizzer.exceptions.QuizzerException;
import it.cascella.quizzer.repository.QuestionRepository;
import it.cascella.quizzer.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final Cache<String, QuizInformations> cache;
    private final TokenGenerator tokenGenerator;
    private final QuestionRepository questionRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, TokenGenerator tokenGenerator, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.questionRepository = questionRepository;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS) // TTL 15 minuti
                .build();
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

    public String generateLink(Integer quizId,Integer numbOfQuestions, Integer durationInMinutes, Integer id, CustomUserDetails details) throws QuizzerException {
        // Check if the quiz exists and belongs to the user
        Quiz quiz = quizRepository.findByIdAndUserId_Id(quizId, details.getId())
                .orElseThrow(() -> new QuizzerException("Quiz not found with id: " + quizId + " for user: " + details.getUsername(), HttpStatus.NOT_FOUND.value()));
        String token = tokenGenerator.generateToken(32);


        //todo insert nel db
        cache.put(token, new QuizInformations(details,
                quizId,
                quiz.getTitle(),
                quiz.getDescription(),
                numbOfQuestions,
                durationInMinutes,
                new ConcurrentHashMap<>()
                )
        );
        return String.format(token);
    }

    public List<GetQuestionDtoNotCorrected> getQuestionFromToken(String token, CustomUserDetails principal) throws QuizzerException {
        QuizInformations quizInformations = cache.getIfPresent(token);
        Integer quizId = quizInformations.getQuizId();
        if (quizInformations.getUsersTakingTheQuiz().containsKey(principal)) {
            throw new QuizzerException("User has already taken the quiz", HttpStatus.FORBIDDEN.value());
        }
        quizInformations.getUsersTakingTheQuiz().put(principal,new QuizUserInformation( QuizUserInformation.Status.IN_PROGRESS,-1, new Date(), null));

        List<Question> questions = questionRepository.findRandomQuestions(quizInformations.getNumberOfQuestions(),quizId,quizInformations.getOwner().getId());
        //todo insert nel db

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
        //TODO SALVARE SU DB IL TOKEN è INUTILE QUA IN CACHE
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
