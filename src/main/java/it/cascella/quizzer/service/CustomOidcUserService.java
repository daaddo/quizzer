package it.cascella.quizzer.service;


import it.cascella.quizzer.entities.Role;
import it.cascella.quizzer.entities.Users;
import it.cascella.quizzer.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    public CustomOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // delega al default loader
        OidcUser oidcUser = super.loadUser(userRequest);

        // estrai info dal token
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();

        Optional<Users> byEmail = userRepository.findUsersByEmail(email);
        // salva o aggiorna nel DB
        // log del TOKENID
        if (byEmail.isPresent()) {

            log.info("Users found with email {} and name {} and tokenID:{} ", email, name, oidcUser.getIdToken().getTokenValue());
        }
        if (byEmail.isEmpty()){
            Users user = new Users();
            user.setEmail(email);
            user.setUsername(name);
            user.setEnabled(true);
            user.setRole(Role.USER);
            user.setTokenId(oidcUser.getIdToken());
            user.setProfilePictureUrl(oidcUser.getPicture());
            userRepository.save(user);
            byEmail = Optional.of(user);
        }

        return byEmail.get();
    }
}
