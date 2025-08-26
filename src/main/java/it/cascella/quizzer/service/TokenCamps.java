package it.cascella.quizzer.service;

import lombok.Getter;

@Getter
public enum TokenCamps {
    ISSUER("iss"),
    SUBJECT("sub"),
    AUDIENCE("aud"),
    EXPIRATION("exp"),
    NOT_BEFORE("nbf"),
    ISSUED_AT("iat"),
    ID("jti"),
    EMAIL("email");
    private final String claimName;

    TokenCamps(String claimName) {
        this.claimName = claimName;
    }
}
