package it.cascella.quizzer.service;


import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class TokenGenerator {
    private  final SecureRandom secureRandom = new SecureRandom();


    public  String generateToken(int lengthBytes) {
        byte[] token = new byte[lengthBytes];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token).substring(0, lengthBytes);
    }


}
