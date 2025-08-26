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

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            String authHeader = request.getHeader("Authorization");
            String jwtToken ;
            String username ;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7);
                try{
                    username = jwtService.extractUsername(jwtToken);
                    CustomUserDetails user = jwtService.getUser(username);
                    if (user == null) {
                        // 1 validare il token
                        // 1.1 controllare che il token non sia scaduto
                        // 1.2 controllare che sia firmato con la nostra chiave
                        // 2 controllare che l utente sia salvato nel db
                        // 3 controllare che l utente non sia gia autenticato (gia fatto)
                        // 4 controllare che l utente sia abilitato
                        throw new UsernameNotFoundException("User " + username + " not found");
                    }
                    if (jwtService.isTokenValid(jwtToken, username)){
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }catch (Exception _){

                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
