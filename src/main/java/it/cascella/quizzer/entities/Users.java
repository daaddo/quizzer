package it.cascella.quizzer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
        return id;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return CustomUserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return CustomUserDetails.super.isEnabled();
    }

}
