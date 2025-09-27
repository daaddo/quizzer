package it.cascella.quizzer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Entity
public class Users implements CustomUserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @ColumnDefault("1")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "profile_picture_url", length = 2048)
    private  String profilePictureUrl;


    @Transient
    private OidcIdToken tokenId;


    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.getName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return CustomUserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public Integer getId() {
        return this.id;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return CustomUserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return CustomUserDetails.super.isEnabled();
    }

    @Override
    public Map<String, Object> getClaims() {
        return Map.of();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return new OidcUserInfo(Map.of(
                "sub", String.valueOf(this.id),                 // identificativo unico interno
                "name", this.username,                          // nome visualizzato
                "email", this.email,                            // email
                "email_verified", this.enabled,                 // flag email verificata
                "picture", this.profilePictureUrl               // immagine profilo
        ));
    }

    @Override
    public OidcIdToken getIdToken() {
        return tokenId;
    }

    /**
     * Returns the name of the authenticated <code>Principal</code>. Never
     * <code>null</code>.
     *
     * @return the name of the authenticated <code>Principal</code>
     */
    @Override
    public String getName() {
        return getUsername();
    }

}
