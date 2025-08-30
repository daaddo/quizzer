package it.cascella.quizzer.controller;


import it.cascella.quizzer.dtos.CreateQuestionDto;
import it.cascella.quizzer.dtos.GetQuestionDto;
import it.cascella.quizzer.dtos.PutQuestionDTO;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/{quizId}")
    public ResponseEntity<List<GetQuestionDto>> getAllQuestions(@AuthenticationPrincipal CustomUserDetails principal  , @PathVariable Integer quizId) {
        return ResponseEntity.ok(questionService.getAllQuestions(principal.getId(),quizId));
    }

    @GetMapping("/random")
    public ResponseEntity<List<GetQuestionDto>> getRandomSetOfQuestions(@RequestParam Integer size, @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("Fetching a random set of {} questions", size);
        log.info("Fetching for {}", principal);
        return ResponseEntity.ok(questionService.getRandomSetOfQuestions(size,principal.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails principal) {
        questionService.deleteQuestion(id,principal.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<String> updateQuestion(@RequestBody PutQuestionDTO putQuestionDTO, @AuthenticationPrincipal CustomUserDetails principal) {
        questionService.updateQuestion(putQuestionDTO, principal.getId());
        return ResponseEntity.ok("Question updated successfully");
    }

}
