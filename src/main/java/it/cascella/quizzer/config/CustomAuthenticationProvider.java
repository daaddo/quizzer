package it.cascella.quizzer.config;

import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;


    @Autowired
    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("User {} requested Authentication", username);
        // Simuliamo un utente (in pratica dovresti usare UserDetailsService per recuperarlo dal DB)
        Users user = userService.loadUserByUsernameOrEmail(username);
        if (user == null || password == null || password.isEmpty() || username.isEmpty()) {
            throw new BadCredentialsException("Credenziali non valide");
        }
        if ((username.equals(user.getUsername()) || username.equals(user.getEmail())) && user.isAccountNonLocked() && passwordEncoder.matches(password, user.getPassword())) {
            log.info("User {} authenticated successfully", username);
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Credenziali non valide");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
