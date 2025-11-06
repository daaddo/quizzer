package it.cascella.quizzer.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/csrf")
public class CSRFController {

    @GetMapping
    public CsrfToken getCSRFToken(CsrfToken token) {
        return token;
    }
}
