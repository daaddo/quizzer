package it.cascella.quizzer.config;


import it.cascella.quizzer.filters.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfigurator {
    private final JwtFilter jwtFilter;

    @Value("${allowed.origins}" )
    private String allowedOrigins;


    private AuthenticationProvider authenticationProvider;


    @Autowired
    public SecurityConfigurator(JwtFilter jwtFilter, AuthenticationProvider authenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(cc -> cc.disable());
        http.cors((cors) -> cors.configurationSource(corsConfigurationSource()));
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/jwt/user/**").permitAll()
                .requestMatchers("/api/v1/**").authenticated()
        );
        http.authenticationProvider(authenticationProvider);
        http.formLogin(withDefaults());

        http.sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //NON VOGLIAMO SESSIONI PERSISTENTI MA OGNI RICHIESTA DEVE ESSERE AUTENTICATA
                        .sessionFixation().newSession()
                        .invalidSessionUrl("/timeout")
                        .maximumSessions(3) //numero massimo di sessioni per utente
                        .maxSessionsPreventsLogin(false) // l'effetto sarà che se due utenti sono già loggati al terzo non verrà permesso l'accesso
                        .expiredUrl("/expired")
                //pagina a cui verrà reindirizzato l'utente se la sessione è scaduta
        );
        http.httpBasic(withDefaults());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Allowed origins: {}", allowedOrigins);
        List<String> split = List.of(allowedOrigins.split(","));
        CorsConfiguration configuration = new CorsConfiguration();
        if (allowedOrigins.equals("*")){
            log.info("Setting allowCredentials to false");
            configuration.setAllowedOriginPatterns(List.of("*")); // Usa allowedOriginPatterns
            configuration.setAllowCredentials(false);

        }else{
            configuration.setAllowCredentials(true);
            configuration.setAllowedOrigins(split);
        }
        // Aggiungi il tuo frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "X-XSRF-TOKEN",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Methods",
                "Access-Control-Allow-Credentials",
                "Access-Control-Expose-Headers",
                "Access-Control-Max-Age",
                "Access-Control-Request-Headers",
                "Access-Control-Request-Method",
                "Origin"));

        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "X-XSRF-TOKEN",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Methods",
                "Access-Control-Allow-Credentials",
                "Access-Control-Expose-Headers",
                "Access-Control-Max-Age",
                "Access-Control-Request-Headers",
                "Access-Control-Request-Method",
                "Origin"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
