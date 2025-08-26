package it.cascella.quizzer.controller;


import it.cascella.quizzer.dtos.CreateQuestionDto;
import it.cascella.quizzer.dtos.GetQuestionDto;
import it.cascella.quizzer.dtos.PutQuestionDTO;
import it.cascella.quizzer.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public ResponseEntity<Integer> createQuestion(@RequestBody CreateQuestionDto createQuestionDto,Principal principal) {

        return ResponseEntity.ok(questionService.createQuestion(createQuestionDto,principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<GetQuestionDto>> getAllQuestions(Principal principal) {
        return ResponseEntity.ok(questionService.getAllQuestions(principal.getName()));
    }

    @GetMapping("/random")
    public ResponseEntity<List<GetQuestionDto>> getRandomSetOfQuestions(@RequestParam Integer size, Principal principal) {
        log.info("Fetching a random set of {} questions", size);
        log.info("Fetching for {}", principal);
        return ResponseEntity.ok(questionService.getRandomSetOfQuestions(size,principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer id, Principal principal) {
        questionService.deleteQuestion(id,principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<String> updateQuestion(@RequestBody PutQuestionDTO putQuestionDTO, Principal principal) {
        questionService.updateQuestion(putQuestionDTO, principal.getName());
        return ResponseEntity.ok("Question updated successfully");
    }

}
