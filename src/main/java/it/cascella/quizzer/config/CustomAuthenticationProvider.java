package it.cascella.quizzer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userService;


    @Autowired
    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("User {} requested Authentication", username);
        // Simuliamo un utente (in pratica dovresti usare UserDetailsService per recuperarlo dal DB)
        UserDetails user = userService.loadUserByUsername(username);
        if (user == null || password == null || password.isEmpty() || username.isEmpty()) {
            throw new BadCredentialsException("Credenziali non valide");
        }

        if (username.equals(user.getUsername()) && passwordEncoder.matches(password, user.getPassword())) {
            if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
                throw new BadCredentialsException("Credenziali non valide - nessun ruolo associato");
            }
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
