package it.cascella.quizzer.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import it.cascella.quizzer.config.CustomAuthenticationProvider;
import it.cascella.quizzer.entities.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    private final CustomAuthenticationProvider authProvider;
    private final UserService userService;
    @Value("${jwt.secretKey}")
    private String configuredSecret;
    private final SecretKey secretKey;

    @Autowired
    public JwtService(CustomAuthenticationProvider authProvider, UserService userService) {
        this.authProvider = authProvider;
        this.userService = userService;
        this.secretKey = initializeKey();
    }


    public boolean isUserValid(String username, String password) {
        Authentication authenticate = authProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return authenticate.isAuthenticated();

    }

    public CustomUserDetails getUser(String username) {
        return userService.loadUserByUsername(username);
    }

    public String generateToken(CustomUserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("id", user.getId());
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .and()
                .signWith(secretKey)
                .compact();

    }

    private SecretKey initializeKey() {
        log.info("La chiave : {}", configuredSecret);
        if (configuredSecret != null && !configuredSecret.isEmpty()) {
            log.info("Using configured JWT secret key");
            log.info("Configured JWT secret (Base64): " + configuredSecret);
            // Se la chiave è in formato Base64, decodificala prima
            try {
                byte[] keyBytes = Decoders.BASE64URL.decode(configuredSecret);
                return Keys.hmacShaKeyFor(keyBytes);
            } catch (Exception e) {
                // Se non è in Base64, usala come stringa normale
                log.warn("JWT secret non è in formato Base64, usando come stringa normale");
                return Keys.hmacShaKeyFor(configuredSecret.getBytes());
            }
        } else {
            // Genera una chiave se non configurata
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
                keyGenerator.init(256);
                log.info("Generated new JWT secret key " + Encoders.BASE64URL.encode(keyGenerator.generateKey().getEncoded()));
                return keyGenerator.generateKey();
            } catch (NoSuchAlgorithmException e) {
                log.error("Error creating key generator", e);
                throw new RuntimeException(e);
            }
        }
    }

    public Object getClaim(String token, TokenCamps campo) {
        Claims claims = extractAllClaims(token);
        return claims.get(campo.getClaimName());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims payload = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if(payload.getExpiration().before(new Date())) {
            throw new ExpiredJwtException(null, payload, "Token expired");
        }
        return payload;
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}