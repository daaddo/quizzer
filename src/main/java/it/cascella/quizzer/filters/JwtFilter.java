package it.cascella.quizzer.filters;

import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    @Autowired
    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //IL Sec SecurityContextHolder.getContext().getAuthentication() controlla che non si sia gia autenticati per
        // la richiesta corrente nella stateless non dovrebbe mai avvenire tuttavia per gli altri tipi di state è possibile
        // che l utente sia autenticato prima di arrivare a questo filtro
        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        String jwtToken ;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            String username = jwtService.extractUsername(jwtToken);
            log.info("Token is valid for user "+username);
            CustomUserDetails user;
            try {
                user = jwtService.getUser(username);
            } catch (UsernameNotFoundException e) {
                log.error("User not found: "+username);
                filterChain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }else {
            filterChain.doFilter(request, response);
        }


    }
}
