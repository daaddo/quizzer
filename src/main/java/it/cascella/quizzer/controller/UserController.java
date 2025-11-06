package it.cascella.quizzer.controller;


import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.exceptions.QuizzerException;
import it.cascella.quizzer.service.MailService;
import it.cascella.quizzer.service.SendMailGrpcClientService;
import it.cascella.quizzer.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final ExecutorService executorService = new ThreadPoolExecutor(
            1, // core pool size (max thread attivi in parallelo)
            1, // max pool size
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(50) // coda max 50 richieste in attesa
    );
    private final SendMailGrpcClientService sendMailGrpcClientService;

    @Autowired
    public UserController(UserService userService, MailService mailService, SendMailGrpcClientService sendMailGrpcClientService) {
        this.userService = userService;
        this.mailService = mailService;
        this.sendMailGrpcClientService = sendMailGrpcClientService;
    }

    @GetMapping
    public ResponseEntity<UserInformationDTO> getUserInformation(@AuthenticationPrincipal CustomUserDetails principal) {
        // Assuming you have a service to fetch user information
        UserInformationDTO userInfo = userService.getUserInformation(principal);
        log.info("returning user info for user: {} infos: {}", principal.getUsername(),userInfo);
        return ResponseEntity.ok(userInfo);
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody NewUserDTO newUserDTO) throws QuizzerException {
        log.info("Registering new user with username: {}", newUserDTO.username());
        String token = userService.registerUser(newUserDTO);
        Mail mail = new Mail(
                newUserDTO.email(),
                "Confirm your registration",
                "Please confirm your registration by clicking the following link: "
                        + userService.getURL() + "/api/v1/users/confirm?token=" + token
        );
        sendMailGrpcClientService.getNewFutureStub().sendMail(it.cascella.sendmail.proto.MailRequest.newBuilder()
                        .setTo(mail.getTo())
                        .setSubject(mail.getSubject())
                        .setBody(mail.getBody())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {

        try {
            Future<ResponseEntity<String>> submit = executorService.submit(() -> {
                log.info("Confirming user with token: {}", token);
                boolean confirmed = userService.confirmUser(token);
                if (confirmed) {
                    return ResponseEntity.ok("User confirmed successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
                }
            });
            return submit.get();
        } catch (RejectedExecutionException e) {
            // La coda è piena → troppi in attesa
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Troppi utenti in coda, riprova più tardi");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore interno");
        }

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam("email") String email) throws QuizzerException {
        log.info("Password reset requested for email: {}", email);
        String token = userService.initiatePasswordReset(email);
        Mail mail = new Mail(
                email,
                "Password reset request",
                "Click on the following link to reset your password: " + userService.getURL() + "/reset-password?token=" + token);
        sendMailGrpcClientService.getNewFutureStub().sendMail(
                it.cascella.sendmail.proto.MailRequest.newBuilder()
                        .setTo(mail.getTo())
                        .setSubject(mail.getSubject())
                        .setBody(mail.getBody())
                        .build()
        );
        return ResponseEntity.ok().build();
    }

    private record ResetPasswordRequest(@NotNull String token, @NotNull @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "La password deve contenere almeno 1 minuscola, 1 maiuscola e 1 numero"
    )
    @Size(min = 8, message = "La password deve essere di almeno 8 caratteri") String newPassword) {}
    @PostMapping("/set/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) throws QuizzerException {
        log.info("Resetting password with token: {}", request.token());
        userService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok("Password reset successfully");

    }

    @GetMapping("/amIauthenticated")
    public ResponseEntity<String> amIAuthenticated(@AuthenticationPrincipal CustomUserDetails principal) {
        log.info("Checking authentication for user: {}", principal.getUsername());
        return ResponseEntity.ok("User is authenticated");
    }


    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkAuthentication(
            HttpServletRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Map<String, Object> response = new HashMap<>();
        boolean hasRememberMeCookie = false;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember-me".equals(cookie.getName())) {
                    hasRememberMeCookie = true;
                    break;
                }
            }
        }

        if (principal != null) {
            log.info("Authenticated check for user: {}", principal.getUsername());
            response.put("authenticated", true);
            response.put("username", principal.getUsername());
            response.put("hasRememberMeCookie", hasRememberMeCookie);
            return ResponseEntity.ok(response);
        } else {
            log.info("Authenticated check: no authenticated user");
            response.put("authenticated", false);
            response.put("hasRememberMeCookie", hasRememberMeCookie);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    @GetMapping("/issued-quizzes/{quizId}")
    public ResponseEntity<List<IssuedQuizInfosDto>> getIssuedQuizzes(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable Integer quizId) {
        return ResponseEntity.ok(userService.getIssuedQuizzes(principal, quizId));
    }

    @GetMapping("/issued-quizzes-infos/{token}")
    public ResponseEntity<List<UserQuizAttemptDto>> getIssuedQuizInfos(@AuthenticationPrincipal CustomUserDetails principal, @PathVariable String token) throws QuizzerException {
        return ResponseEntity.ok(userService.getIssuedQuizInfo(principal, token));
    }
}
