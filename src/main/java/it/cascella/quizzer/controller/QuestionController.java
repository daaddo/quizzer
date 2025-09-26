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
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
    public ResponseEntity<Integer> createQuestion(@RequestBody CreateQuestionDto createQuestionDto,@AuthenticationPrincipal CustomUserDetails principal) {

        return ResponseEntity.ok(questionService.createQuestion(createQuestionDto,principal.getId()));
    }

    /***
     * Get all questions for a specific quiz for the authenticated user
     * @param principal (the authenticated user)
     * @param quizId the id of the quiz to get questions for
     * @return a list of questions
     */
    @GetMapping("/{quizId}")
    public ResponseEntity<List<GetQuestionDto>> getAllQuestions(@AuthenticationPrincipal CustomUserDetails principal  ,
                                                                @PathVariable Integer quizId) {

        log.info("principal : "+principal.getId());
        return ResponseEntity.ok(questionService.getAllQuestions(principal.getId(),quizId));
    }

    /***
     * Get a random set of questions for a specific quiz for the authenticated user
     * @param size the number of questions to retrieve
     * @param quizId the id of the quiz to get questions for
     * @param principal (the authenticated user)
     * @return a list of questions
     */
    @GetMapping("/random")
    public ResponseEntity<List<GetQuestionDto>> getRandomSetOfQuestions(@RequestParam Integer size, @RequestParam Integer quizId, @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("Fetching a random set of {} questions", size);
        log.info("Fetching for {}", principal);
        return ResponseEntity.ok(questionService.getRandomSetOfQuestions(size,quizId,principal.getId()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails principal) {
        questionService.deleteQuestion(id,principal.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<String> updateQuestion(@RequestBody PutQuestionDTO putQuestionDTO, @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("Updating question with id {} for user {} with id {}", putQuestionDTO.id(), principal.getUsername(), principal.getId());
        questionService.updateQuestion(putQuestionDTO, principal.getId());
        return ResponseEntity.ok("Question updated successfully ");
    }

}
