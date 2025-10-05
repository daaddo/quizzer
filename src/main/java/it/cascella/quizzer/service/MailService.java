package it.cascella.quizzer.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.cascella.quizzer.dtos.NewUserDTO;
import it.cascella.quizzer.entities.Role;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.exceptions.QuizzerException;
import it.cascella.quizzer.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class MailService {

    private final  JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String FROM ;

    private final Cache<String, NewUserDTO> cacheRegister;
    private final Cache<String, NewUserDTO> cacheReset;
    private final TokenGenerator tokenGenerator;
    private final UserRepository userRepository;

    @Value("${register.url}" )
    private  String URL ;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public MailService(JavaMailSender javaMailSender, TokenGenerator tokenGenerator, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.javaMailSender = javaMailSender;
        this.tokenGenerator = tokenGenerator;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cacheReset = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES) // TTL 15 minuti
                .build();
        this.cacheRegister = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES) // TTL 15 minuti
                .build();

    }

    public void registerUser(NewUserDTO newUserDTO) throws QuizzerException {
        Optional<Users> usersByUsernameOrEmail = userRepository.getUsersByUsernameOrEmail(newUserDTO.username(), newUserDTO.email());
        if (usersByUsernameOrEmail.isPresent()) {
            if (usersByUsernameOrEmail.get().getUsername().equals(newUserDTO.username())) {
                throw new QuizzerException("Username already in use", HttpStatus.CONFLICT.value());
            }
            throw new QuizzerException("email already in use", HttpStatus.CONFLICT.value());
        }
        String token = tokenGenerator.generateToken(32);

        String message = "Click on the following link to verify your email: " + URL + "/verify?token=" + token;
        sendEmail(newUserDTO.email(), "Email Verification", message);

        NewUserDTO toSave = new NewUserDTO(
                newUserDTO.username(),
                passwordEncoder.encode(newUserDTO.password()),
                newUserDTO.email()

        );
        cacheRegister.put(token, toSave);
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            log.info("Sending email to: " + to);
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setFrom(FROM);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {} stacktrace: {} ", to, e.getMessage(), e.getStackTrace());
        }
    }

    public HashMap<String, Object> getCacheStats() {
        HashMap<String, Object> stats = new HashMap<>();
        stats.put("estimatedSize", cacheRegister.estimatedSize());
        stats.put("hitCount", cacheRegister.stats().hitCount());
        stats.put("missCount", cacheRegister.stats().missCount());
        stats.put("hitRate", cacheRegister.stats().hitRate());
        stats.put("missRate", cacheRegister.stats().missRate());
        stats.put("map", cacheRegister.asMap());
        return stats;
    }

    @Transactional
    @Modifying
    public boolean confirmUser(String token) {
        NewUserDTO newUserDTO = cacheRegister.getIfPresent(token);
        if (newUserDTO != null) {
            cacheRegister.invalidate(token);
            Users user = new Users();
            user.setUsername(newUserDTO.username());
            user.setEmail(newUserDTO.email());
            user.setPassword(newUserDTO.password());
            user.setEnabled(true);
            user.setRole(Role.USER);
            userRepository.save(user);
            // Qui puoi aggiungere la logica per salvare l'utente nel database
            log.info("User {} confirmed successfully", newUserDTO.username());
            return true;
        } else {
            log.warn("Invalid or expired token: {}", token);
            return false;
        }
    }

    public boolean initiatePasswordReset(String email) {
        Users user = userRepository.findUsersByEmail(email).orElse(null);
        if (user == null) {
            log.warn("Password reset requested for non-existing email: {}", email);
            return false; // Email non trovata
        }

        String token = tokenGenerator.generateToken(32);
        String message = "Click on the following link to reset your password: " + URL + "/api/v1/users/reset-password?token=" + token;
        sendEmail(email, "Password Reset", message);

        // Salva il token di reset della password in cache con TTL di 15 minuti
        cacheReset.put(token, new NewUserDTO(user.getUsername(), user.getEmail(), user.getPassword()));
        return true;
    }

    public void resetPassword( String token, String s) throws QuizzerException {
        NewUserDTO newUserDTO = cacheReset.getIfPresent(token);
        if (newUserDTO != null) {
            cacheReset.invalidate(token);
            Users user = userRepository.findUsersByEmail(newUserDTO.email()).orElse(null);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(s));
                userRepository.save(user);
                log.info("Password for user {} reset successfully", newUserDTO.username());

            } else {
                log.error("User not found for email: {}", newUserDTO.email());
                throw new QuizzerException("User not found", HttpStatus.NOT_FOUND.value());
            }
        } else {
            log.warn("Invalid or expired password reset token: {}", token);
        }

    }
}
