package it.cascella.quizzer.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.cascella.quizzer.config.CustomPasswordEncoder;
import it.cascella.quizzer.dtos.NewUserDTO;
import it.cascella.quizzer.entities.Role;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class MailService {

    private final  JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String FROM ;

    private final Cache<String, NewUserDTO> cache;
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
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES) // TTL 15 minuti
                .build();

    }

    public void registerUser(NewUserDTO newUserDTO) {
        String token = tokenGenerator.generateToken(32);

        String message = "Click on the following link to verify your email: " + URL + "/api/v1/auth/verify?token=" + token;
        sendEmail(newUserDTO.email(), "Email Verification", message);

        NewUserDTO toSave = new NewUserDTO(
                newUserDTO.username(),
                newUserDTO.email(),
                passwordEncoder.encode(newUserDTO.password())
        );
        cache.put(token, toSave);
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
        stats.put("estimatedSize", cache.estimatedSize());
        stats.put("hitCount", cache.stats().hitCount());
        stats.put("missCount", cache.stats().missCount());
        stats.put("hitRate", cache.stats().hitRate());
        stats.put("missRate", cache.stats().missRate());
        stats.put("map", cache.asMap());
        return stats;
    }

    public boolean confirmUser(String token) {
        NewUserDTO newUserDTO = cache.getIfPresent(token);
        if (newUserDTO != null) {
            cache.invalidate(token);
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
}
