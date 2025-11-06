package it.cascella.quizzer.config;


import it.cascella.quizzer.filters.JwtFilter;
import it.cascella.quizzer.service.CustomOidcUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
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

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${logout.uri}")
    private String googleOIDClogoutUrl;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private final CustomOidcUserService customOidcUserService;
    private AuthenticationProvider authenticationProvider;


    @Autowired
    public SecurityConfigurator(JwtFilter jwtFilter, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomOidcUserService customOidcUserService, AuthenticationProvider authenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customOidcUserService = customOidcUserService;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfToken =CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfToken.setCookieCustomizer(
                cookie -> {
                    cookie.sameSite("Lax");
                }
        );
        http.cors((cors) -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(csrf -> csrf.csrfTokenRepository(csrfToken).ignoringRequestMatchers(
                "/error",
                "/v1/csrf",
                "/api/v1/users/register",
                "/api/v1/users/confirm",
                "/api/v1/users/forgot-password",
                "/api/v1/users/set/reset-password/**",
                "/api/v1/users/status",
                "/actuator/**",
                "/api/v1/jwt/user/**"
        ));
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("api/v1/csrf").permitAll()
                .requestMatchers("/api/v1/users/register", "/api/v1/users/confirm","/api/v1/users/forgot-password","/api/v1/users/set/reset-password/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/users/status").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/v1/jwt/user/**").permitAll()
                .requestMatchers("/api/v1/users/private","/api/v1/users/privatepost").authenticated()
                .requestMatchers("/api/v1/**","/logout").authenticated()
        );
        http.authenticationProvider(authenticationProvider);
        http.formLogin(form -> form.disable());

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") // dove reindirizzare dopo il logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
        );

        http.sessionManagement(sessionManagement -> sessionManagement
                         //NON VOGLIAMO SESSIONI PERSISTENTI MA OGNI RICHIESTA DEVE ESSERE AUTENTICATA
                        .sessionFixation().newSession()
                        .invalidSessionUrl("/timeout")
                        .maximumSessions(3) //numero massimo di sessioni per utente
                        .maxSessionsPreventsLogin(false) // l'effetto sarà che se due utenti sono già loggati al terzo non verrà permesso l'accesso
                        .expiredUrl("/expired")
                //pagina a cui verrà reindirizzato l'utente se la sessione è scaduta
        );
        log.info("frontend url: {}", frontendUrl);
        http.oauth2Login(oauth -> oauth
                .userInfoEndpoint(userInfo -> userInfo
                        .oidcUserService(customOidcUserService)
                )
                .defaultSuccessUrl(frontendUrl, true)

        );
        http.logout(logout -> logout

                .logoutSuccessHandler(oidcLogoutSuccessHandler())
        );
        http.exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint)
        );
        http.httpBasic(withDefaults());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) -> {

            log.info("Performing OIDC logout");
            String logoutUrl = googleOIDClogoutUrl;
            String redirectUri = "http://localhost:8080/"; // dove tornare dopo logout

            response.sendRedirect(logoutUrl + "?redirect_uri=" + redirectUri);
        };
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
                "X-XSRF-TOKEN",
                "Accept",
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
                "X-XSRF-TOKEN",
                "Accept",
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
