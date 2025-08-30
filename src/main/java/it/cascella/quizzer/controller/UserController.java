package it.cascella.quizzer.controller;


import it.cascella.quizzer.config.CustomAuthenticationProvider;
import it.cascella.quizzer.dtos.UserInformationDTO;
import it.cascella.quizzer.entities.CustomUserDetails;
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
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public UserController(UserService userService, CustomAuthenticationProvider customAuthenticationProvider) {
        this.userService = userService;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @GetMapping
    public ResponseEntity<UserInformationDTO> getUserInformation(@AuthenticationPrincipal CustomUserDetails principal) {
        // Assuming you have a service to fetch user information
        UserInformationDTO userInfo = userService.getUserInformation(principal);
        return ResponseEntity.ok(userInfo);
    }
}
