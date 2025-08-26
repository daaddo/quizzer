package it.cascella.quizzer.controller;


import it.cascella.quizzer.dtos.UserInformationDTO;
import it.cascella.quizzer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserInformationDTO> getUserInformation(Principal principal) {
        // Assuming you have a service to fetch user information
        UserInformationDTO userInfo = userService.getUserInformation(principal.getName());
        return ResponseEntity.ok(userInfo);
    }
}
