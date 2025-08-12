package it.cascella.quizzer.service;


import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.Answer;
import it.cascella.quizzer.entities.Question;
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

    @Autowired
    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public Integer createQuestion(CreateQuestionDto createQuestionDto) {

        Question question = new Question();
        System.out.println("Creating question with title: " + createQuestionDto.title());
        System.out.println("Question text: " + createQuestionDto.question());

        question.setTitle(createQuestionDto.title());
        question.setQuestion(createQuestionDto.question());
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

    public List<GetQuestionDto> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
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

    public void deleteQuestion(Integer id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        questionRepository.deleteQuestionById(id);

    }


    @Modifying
    @Transactional
    public void updateQuestion(PutQuestionDTO putQuestionDTO) {
        log.info("payload: "+putQuestionDTO);
        questionRepository.updateQuestion(putQuestionDTO.title(), putQuestionDTO.question(), putQuestionDTO.id());

    }
}
