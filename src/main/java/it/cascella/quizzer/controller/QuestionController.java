package it.cascella.quizzer.controller;


import it.cascella.quizzer.dtos.CreateQuestionDto;
import it.cascella.quizzer.dtos.GetQuestionDto;
import it.cascella.quizzer.dtos.PutQuestionDTO;
import it.cascella.quizzer.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<Integer> createQuestion(@RequestBody CreateQuestionDto createQuestionDto) {
        return ResponseEntity.ok(questionService.createQuestion(createQuestionDto));
    }

    @GetMapping
    public ResponseEntity<List<GetQuestionDto>> getAllQuestions() {
        // Placeholder for future implementation
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/random")
    public ResponseEntity<List<GetQuestionDto>> getRandomSetOfQuestions(@RequestParam Integer size) {
        log.info("Fetching a random set of {} questions", size);
        return ResponseEntity.ok(questionService.getRandomSetOfQuestions(size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<String> updateQuestion(@RequestBody PutQuestionDTO putQuestionDTO) {
        questionService.updateQuestion(putQuestionDTO);
        return ResponseEntity.ok("Question updated successfully");
    }

}
