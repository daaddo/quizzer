package it.cascella.quizzer.controller;


import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.service.JwtService;
import it.cascella.quizzer.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/jwt/user")
@Slf4j
public class UserJwtController {
    private final JwtService jwtService;
    private final UserService userService;

    public UserJwtController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public record LoginRequest(String username, String password) {}
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.username();
        String password = loginRequest.password();
        log.info("Login request received: " + username );
        if (!jwtService.isUserValid(username,password)){
            log.info("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        CustomUserDetails timerUserDetails = userService.loadUserByUsername(username);
        String token= jwtService.generateToken(timerUserDetails);
        return ResponseEntity.ok(token);
    }

    @GetMapping
    public ResponseEntity<?> response(
            Principal principal
    ){
        return ResponseEntity.ok("hello from the server!"+ principal.getName()+" "+principal.toString());
    }
}
