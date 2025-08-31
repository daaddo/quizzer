package it.cascella.quizzer.service;

import it.cascella.quizzer.dtos.NewUserDTO;
import it.cascella.quizzer.dtos.QuizInformationDTO;
import it.cascella.quizzer.dtos.UserInformationDTO;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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


}
