package it.cascella.quizzer.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
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
    @Value("${allowed.origins}" )
    private String allowedOrigins;

    @Value("${remember.me.key}")
    private String rememberMe;

    @Value("${remember.me.secure}")
    private boolean secure;

    private AuthenticationProvider authenticationProvider;


    @Autowired
    public SecurityConfigurator(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(cc -> cc.disable());
        http.cors((cors) -> cors.configurationSource(corsConfigurationSource()));
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/user/id","/api/v1/health/logged","/actuator/env").authenticated()
                .requestMatchers(
                        "/api/v1/**",
                        "/api/v1/generalLayouts/**",
                        "/api/v1/rooms/**",
                        "/api/v1/reservation/**",
                        "/api/v1/table/**",
                        "/api/v1/clients/**",
                        "/api/v1/clients/smart-search/**",
                        "/api/v1/health/**",
                        "/api/v2/**",
                        "/actuator/**",
                        "/error"
                ).permitAll()
                .requestMatchers("/api/v1/health/logged","/actuator/env").authenticated()

        );
        http.authenticationProvider(authenticationProvider);
        http.formLogin(withDefaults());

        http.rememberMe(r -> r
                .key(rememberMe)
                .tokenValiditySeconds(3600*24*7)
                .useSecureCookie(secure)
        ).userDetailsService(userService);

        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionFixation().newSession()
                .invalidSessionUrl("/timeout")
                .maximumSessions(5)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/expired")
        );

        http.addFilterBefore(requestLoggingFilter, AuthorizationFilter.class);
        http.httpBasic(withDefaults());

        return http.build();
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
