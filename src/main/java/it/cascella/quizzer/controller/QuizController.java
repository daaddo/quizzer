package it.cascella.quizzer.controller;

import io.jsonwebtoken.Jwt;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
}
