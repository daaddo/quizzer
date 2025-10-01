package it.cascella.quizzer.service;

import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.entities.IssuedQuiz;
import it.cascella.quizzer.entities.UserQuizAttempt;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.exceptions.QuizzerException;
import it.cascella.quizzer.repository.IssuedQuizRepository;
import it.cascella.quizzer.repository.UserQuizAttemptRepository;
import it.cascella.quizzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service

public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final IssuedQuizRepository issuedQuizRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;

    @Autowired
    public UserService(UserRepository userRepository, IssuedQuizRepository issuedQuizRepository, UserQuizAttemptRepository userQuizAttemptRepository) {
        this.userRepository = userRepository;
        this.issuedQuizRepository = issuedQuizRepository;
        this.userQuizAttemptRepository = userQuizAttemptRepository;
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
        return new UserInformationDTO(user.getId(), user.getUsername(), quizzes);
    }



    public List<IssuedQuizInfosDto> getIssuedQuizzes(CustomUserDetails principal, Integer quizId) {
        return issuedQuizRepository.getIssuedQuiz(principal.getId(), quizId);
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
}
