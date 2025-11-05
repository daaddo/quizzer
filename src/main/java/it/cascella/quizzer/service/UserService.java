package it.cascella.quizzer.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.entities.IssuedQuiz;
import it.cascella.quizzer.entities.Role;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.exceptions.QuizzerException;
import it.cascella.quizzer.repository.IssuedQuizRepository;
import it.cascella.quizzer.repository.UserQuizAttemptRepository;
import it.cascella.quizzer.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service

public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final IssuedQuizRepository issuedQuizRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;
    private final TokenGenerator tokenGenerator;
    private final Cache<String, NewUserDTO> cacheRegister = Caffeine.newBuilder()
            .expireAfterWrite(15, java.util.concurrent.TimeUnit.MINUTES) // TTL 15 minuti
            .build();
    private final Cache<String, NewUserDTO> cacheReset = Caffeine.newBuilder()
            .expireAfterWrite(15, java.util.concurrent.TimeUnit.MINUTES) // TTL 15 minuti
            .build();;

    private final PasswordEncoder passwordEncoder;

    @Value("${register.url}" )
    @Getter
    private  String URL ;

    @Autowired
    public UserService(UserRepository userRepository, IssuedQuizRepository issuedQuizRepository, UserQuizAttemptRepository userQuizAttemptRepository, TokenGenerator tokenGenerator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.issuedQuizRepository = issuedQuizRepository;
        this.userQuizAttemptRepository = userQuizAttemptRepository;
        this.tokenGenerator = tokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

    public Users loadUserByUsernameOrEmail(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

    public UserInformationDTO getUserInformation(CustomUserDetails user) {
        List<QuizInformationDTO> quizzes = userRepository.getQuizInformationsByUserId(user.getId());
        return new UserInformationDTO(user.getId(), user.getUsername(),user.getEmail(), quizzes);
    }



    public List<IssuedQuizInfosDto> getIssuedQuizzes(CustomUserDetails principal, Integer quizId) {
        List<IssuedQuizInfosDto> issuedQuiz = issuedQuizRepository.getIssuedQuiz(principal.getId(), quizId);
        if (Objects.isNull(issuedQuiz) || issuedQuiz.isEmpty()) {
            return List.of();
        }
        log.info("token: {} expire at {}, other tostring {}", issuedQuiz.getFirst().getTokenId(),issuedQuiz.getFirst().getExpiresAt(),issuedQuiz.getFirst().getTokenId().toString());
        return issuedQuiz;
    }

    public List<UserQuizAttemptDto> getIssuedQuizInfo(CustomUserDetails principal, String token) throws QuizzerException {
        Optional<IssuedQuiz> byTokenId = issuedQuizRepository.getByTokenId(token);

        if (byTokenId.isEmpty()){
            throw new QuizzerException("No issued quiz found for token: " + token, HttpStatus.NOT_FOUND.value());
        }
        if (!Objects.equals(byTokenId.get().getIssuer().getId(), principal.getId())) {
            throw new QuizzerException("Not authorized to see this quiz answers: " + token, HttpStatus.FORBIDDEN.value());
        }

        List<UserQuizAttemptDto> allByToken = userQuizAttemptRepository.getAllByToken(token);

        return allByToken;


    }

    public String registerUser(NewUserDTO newUserDTO) throws QuizzerException {
        Optional<Users> usersByUsernameOrEmail = userRepository.getUsersByUsernameOrEmail(newUserDTO.username(), newUserDTO.email());
        if (usersByUsernameOrEmail.isPresent()) {
            if (usersByUsernameOrEmail.get().getUsername().equals(newUserDTO.username())) {
                throw new QuizzerException("Username already in use", HttpStatus.CONFLICT.value());
            }
            throw new QuizzerException("email already in use", HttpStatus.CONFLICT.value());
        }

        String token = tokenGenerator.generateToken(32);

        NewUserDTO toSave = new NewUserDTO(
                newUserDTO.username(),
                passwordEncoder.encode(newUserDTO.password()),
                newUserDTO.email()

        );
        cacheRegister.put(token, toSave);
        return token;
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
        }
        log.warn("Invalid or expired token: {}", token);
        return false;

    }

    public String initiatePasswordReset(String email) {
        Users user = userRepository.findUsersByEmail(email).orElse(null);
        if (user == null) {
            log.info("Password reset requested for non-existing email : {}, no user found for this email", email);

        }

        log.info("Password reset requested for email: {}, user found: {}", email, user.getUsername());
        // Genera un token di reset della password
        String token = tokenGenerator.generateToken(32);
        String message = "Click on the following link to reset your password: " + URL + "/reset-password?token=" + token;


        // Salva il token di reset della password in cache con TTL di 15 minuti
        cacheReset.put(token, new NewUserDTO(user.getUsername(), user.getPassword(),user.getEmail()));
        return token;
    }

    @Modifying
    @Transactional
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
            throw new QuizzerException("Invalid or expired token", HttpStatus.BAD_REQUEST.value());
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
}
