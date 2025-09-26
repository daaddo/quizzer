package it.cascella.quizzer.entities;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface CustomUserDetails extends UserDetails, OidcUser {
    String getEmail();
    Integer getId();

}
