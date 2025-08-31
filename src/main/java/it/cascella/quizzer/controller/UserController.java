package it.cascella.quizzer.controller;


import it.cascella.quizzer.config.CustomAuthenticationProvider;
import it.cascella.quizzer.dtos.NewUserDTO;
import it.cascella.quizzer.dtos.UserInformationDTO;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.service.MailService;
import it.cascella.quizzer.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping
    public ResponseEntity<UserInformationDTO> getUserInformation(@AuthenticationPrincipal CustomUserDetails principal) {
        // Assuming you have a service to fetch user information
        UserInformationDTO userInfo = userService.getUserInformation(principal);
        return ResponseEntity.ok(userInfo);
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody NewUserDTO newUserDTO) {
        log.info("Registering new user with username: {}", newUserDTO.username());
        mailService.registerUser(newUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        log.info("Confirming user with token: {}", token);
        boolean isConfirmed = mailService.confirmUser(token);
        if (isConfirmed) {
            return ResponseEntity.ok("User confirmed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }
    }
}
