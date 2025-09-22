package it.cascella.quizzer.controller;

import io.jsonwebtoken.Jwt;
import it.cascella.quizzer.dtos.NewQuizDTO;
import it.cascella.quizzer.dtos.PutQuizDTO;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {
    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Integer id,  @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("Deleting quiz with id " + id + " for user " + principal.getUsername());
        quizService.deleteQuiz(id,principal.getId());
    }

    @PostMapping
    public ResponseEntity<Integer> insertNewQuiz(@RequestBody NewQuizDTO newQuiz, @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("inserting quiz for user {}",  principal.getUsername());
        return ResponseEntity.ok().body(quizService.insertQuiz(newQuiz,principal.getId()));
    }

    @PutMapping
    public void updateQuiz(@RequestBody PutQuizDTO newQuiz, @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("updating quiz for user {}",  principal.getUsername());
        quizService.updateQuiz(newQuiz,principal.getId());
    }

    record  LinkRequest(
            Integer quizId,
            Integer numberOfQuestions,
            Integer durationInMinutes
    ) {}
    @GetMapping
    public ResponseEntity<String> generateLink(@RequestBody LinkRequest params, @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("generating link for quiz {} for user {}",  params.quizId, principal.getUsername());
        String link= quizService.generateLink(params.quizId, params.durationInMinutes,params.numberOfQuestions(), principal);
        return ResponseEntity.ok(link);
    }
}
