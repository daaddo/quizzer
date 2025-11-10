package it.cascella.quizzer.service;


import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.Answer;
import it.cascella.quizzer.entities.Question;
import it.cascella.quizzer.entities.Quiz;
import it.cascella.quizzer.exceptions.QuizzerException;
import it.cascella.quizzer.repository.QuizRepository;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.repository.AnswerRepository;
import it.cascella.quizzer.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizRepository quizRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.quizRepository = quizRepository;
    }

    @Modifying
    @Transactional
    public Integer createQuestion(CreateQuestionDto createQuestionDto, Integer userId) throws QuizzerException {

        Question question = new Question();

        Users users = questionRepository.verifyQuizExistsInUser(createQuestionDto.quizId(), userId)
                .orElseThrow(() -> new QuizzerException("Quiz not found with id: " + createQuestionDto.quizId() + " for user: " + userId, HttpStatus.NOT_FOUND.value()));

        int correctAnswers = 0;
        for (CreateAnswerDto answer : createQuestionDto.answers()) {
            if (answer.isCorrect() != null && answer.isCorrect()) {
                correctAnswers++;
            }
        }
        if (correctAnswers < 1) {
            throw new QuizzerException("At least one correct answer is required", HttpStatus.BAD_REQUEST.value());
        }
        if (correctAnswers >= 2) {
            question.setMultipleChoice(true);
        } else {
            question.setMultipleChoice(false);
        }
        question.setTitle(createQuestionDto.title());
        question.setQuestion(createQuestionDto.question());
        question.setUser(users);
        question.setQuiz(quizRepository.findQuizById(createQuestionDto.quizId()));
        questionRepository.save(question);

        for (CreateAnswerDto answer : createQuestionDto.answers()) {
            Answer answerEntity = new Answer();
            answerEntity.setAnswer(answer.answer());
            answerEntity.setCorrect(answer.isCorrect());
            answerEntity.setQuestion(question);
            answerRepository.save(answerEntity);

        }


        quizRepository.incrementQuestionCount(createQuestionDto.quizId());
        return question.getId();
    }

    public List<GetQuestionDto> getAllQuestions(Integer principal, Integer quizId) throws QuizzerException {
        Quiz quiz = quizRepository.getById(quizId).orElseThrow(() -> new QuizzerException("No quiz found with id: " + quizId, HttpStatus.NOT_FOUND.value()));

        if (!quiz.getIsPublic()) {
            //quiz is private, fetch questions only for the authenticated user
            List<Question> questions = questionRepository.findAllFromPrincipal(principal, quizId);
            return questions.stream()
                    .map(question -> new GetQuestionDto(
                            question.getId(),
                            question.getTitle(),
                            question.getQuestion(),
                            question.getAnswers().stream()
                                    .map(answer -> new GetAnswerDto(answer.getId(), answer.getAnswer(), answer.getCorrect()))
                                    .toList()))
                    .toList();
        }
        // Quiz is public, fetch all questions without user restriction
        List<Question> allByQuizId = questionRepository.findAllByQuiz_Id(quizId);
        return allByQuizId.stream()
                .map(question -> new GetQuestionDto(
                        question.getId(),
                        question.getTitle(),
                        question.getQuestion(),
                        question.getAnswers().stream()
                                .map(answer -> new GetAnswerDto(answer.getId(), answer.getAnswer(), answer.getCorrect()))
                                .toList()))
                .toList();

    }

    public List<GetQuestionDto> getRandomSetOfQuestions(Integer size, Integer quizId, Integer userId) {
        List<Question> questions = questionRepository.findRandomQuestions(size, quizId, userId);
        return questions.stream()
                .map(question -> new GetQuestionDto(
                        question.getId(),
                        question.getTitle(),
                        question.getQuestion(),
                        question.getAnswers().stream()
                                .map(answer -> new GetAnswerDto(answer.getId(), answer.getAnswer(), answer.getCorrect()))
                                .toList()))
                .toList();
    }

    public void deleteQuestion(Integer id, Integer principal) {
        questionRepository.findByIdAndUsername(id, principal)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        questionRepository.deleteQuestionById(id);
        quizRepository.decrementQuestionCount(id);

    }


    @Modifying
    @Transactional
    public void updateQuestion(PutQuestionDTO putQuestionDTO, Integer principal) {
        log.info("payload: " + putQuestionDTO);
        questionRepository.findQuestionByIdAndPrincipal(putQuestionDTO.id(), principal)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + putQuestionDTO.id() + " for user: " + principal));
        Integer i = questionRepository.updateQuestion(putQuestionDTO.title(), putQuestionDTO.question(), putQuestionDTO.id());
        if (i != 1) {
            throw new RuntimeException("Failed to update question with id: " + putQuestionDTO.id());
        }

    }
}
