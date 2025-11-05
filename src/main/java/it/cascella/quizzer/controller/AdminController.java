package it.cascella.quizzer.controller;

import it.cascella.quizzer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/cache")
    public ResponseEntity<HashMap<String,Object>> getCache() {
        return ResponseEntity.ok(userService.getCacheStats());
    }

    //todo endpoint per backup db

    //todo endpoint per monitoraggio stato applicazione (health check, db connection, ecc)

    //todo endpoint per gestione utenti (list, delete, update roles, ecc)

}
