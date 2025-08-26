package it.cascella.quizzer.service;

import it.cascella.quizzer.dtos.UserInformationDTO;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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

    public UserInformationDTO getUserInformation(String name) {

    }
}
