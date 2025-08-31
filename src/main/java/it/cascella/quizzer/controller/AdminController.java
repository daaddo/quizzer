package it.cascella.quizzer.controller;

import it.cascella.quizzer.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController("/api/v1/admin")
public class AdminController {

    private final MailService mailService;

    @Autowired
    public AdminController(MailService mailService) {
        this.mailService = mailService;

    }

    @GetMapping("/cache")
    public ResponseEntity<HashMap<String,Object>> getCache() {
        return ResponseEntity.ok(mailService.getCacheStats());
    }

    //todo endpoint per backup db

    //todo endpoint per monitoraggio stato applicazione (health check, db connection, ecc)

    //todo endpoint per gestione utenti (list, delete, update roles, ecc)

    //


}
