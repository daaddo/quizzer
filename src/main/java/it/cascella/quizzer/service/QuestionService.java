package it.cascella.quizzer.service;


import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.Answer;
import it.cascella.quizzer.entities.Question;
import it.cascella.quizzer.entities.QuizRepository;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.repository.AnswerRepository;
import it.cascella.quizzer.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private  final AnswerRepository answerRepository;
    private final QuizRepository quizRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.quizRepository = quizRepository;
    }

    public Integer createQuestion(CreateQuestionDto createQuestionDto,String principal) {

        Question question = new Question();

        Users users = questionRepository.verifyQuizExistsInUser(createQuestionDto.quizId(), principal)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + createQuestionDto.quizId() + " for user: " + principal));


        question.setTitle(createQuestionDto.title());
        question.setQuestion(createQuestionDto.question());
        question.setUser(users);
        question.setQuiz(quizRepository.findQuizById(createQuestionDto.quizId()));
        questionRepository.save(question);

        for (CreateAnswerDto answer : createQuestionDto.answers()) {
            Answer answerEntity = new Answer();
            answerEntity.setAnswer(answer.answer());
            answerEntity.setCorrect(answer.correct());
            answerEntity.setQuestion(question);
            answerRepository.save(answerEntity);
        }
        return question.getId();
    }

    public List<GetQuestionDto> getAllQuestions(String principal) {
        List<Question> questions = questionRepository.findAllFromPrincipal(principal);
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

    public List<GetQuestionDto> getRandomSetOfQuestions(Integer size,String username) {
        List<Question> questions = questionRepository.findRandomQuestions(size, username);
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

    public void deleteQuestion(Integer id, String principal) {
        questionRepository.findByIdAndUsername(id, principal)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        questionRepository.deleteQuestionById(id);

    }


    @Modifying
    @Transactional
    public void updateQuestion(PutQuestionDTO putQuestionDTO,String principal) {
        log.info("payload: "+putQuestionDTO);
        questionRepository.findQuestionByIdAndPrincipal(putQuestionDTO.id(), principal)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + putQuestionDTO.id() + " for user: " + principal));
        questionRepository.updateQuestion(putQuestionDTO.title(), putQuestionDTO.question(), putQuestionDTO.id());

    }
}
