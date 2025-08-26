package it.cascella.quizzer.controller;


import it.cascella.quizzer.config.CustomAuthenticationProvider;
import it.cascella.quizzer.dtos.UserInformationDTO;
import it.cascella.quizzer.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    public ResponseEntity<UserInformationDTO> getUserInformation(Principal principal) {
        // Assuming you have a service to fetch user information
        UserInformationDTO userInfo = userService.getUserInformation(principal.getName());
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/remember-me-status")
    public ResponseEntity<Map<String, Object>> checkRememberMeStatus(
            HttpServletRequest request,
            Principal principal) {

        Map<String, Object> response = new HashMap<>();

        // Verifica se c'è il cookie remember-me
        Cookie[] cookies = request.getCookies();
        boolean hasRememberMeCookie = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember-me".equals(cookie.getName())) {
                    hasRememberMeCookie = true;
                    break;
                }
            }
        }

        if (principal != null) {
            response.put("authenticated", true);
            response.put("username", principal.getName());
            response.put("hasRememberMeCookie", hasRememberMeCookie);
            response.put("message", "User is authenticated");
            return ResponseEntity.ok(response);
        } else {
            response.put("authenticated", false);
            response.put("hasRememberMeCookie", hasRememberMeCookie);
            response.put("message", hasRememberMeCookie ? "Remember me cookie present but invalid" : "No authentication");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
