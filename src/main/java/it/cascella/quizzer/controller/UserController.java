package it.cascella.quizzer.controller;


import it.cascella.quizzer.dtos.NewUserDTO;
import it.cascella.quizzer.dtos.UserInformationDTO;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.exceptions.QuizzerException;
import it.cascella.quizzer.service.MailService;
import it.cascella.quizzer.service.UserService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        log.info("Confirming user with token: {}", token);
        boolean isConfirmed = mailService.confirmUser(token);
        if (isConfirmed) {
            return ResponseEntity.ok("User confirmed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam("email") String email) {
        log.info("Password reset requested for email: {}", email);
        mailService.initiatePasswordReset(email);
        return ResponseEntity.ok().build();
    }

    private record ResetPasswordRequest(@NotNull String token, @NotNull @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "La password deve contenere almeno 1 minuscola, 1 maiuscola e 1 numero"
    )
    @Size(min = 8, message = "La password deve essere di almeno 8 caratteri") String newPassword) {

    }
    @PostMapping("/set/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) throws QuizzerException {
        log.info("Resetting password with token: {}", request.token());
        mailService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok("Password reset successfully");

    }
}
