package it.cascella.quizzer.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

@Slf4j
public record UserQuizAttemptDto(
        Integer id,
        Integer userId,
        byte[] token,
        Double score,
        Timestamp attemptedAt,
        Timestamp finishedAt,
        String status,
        String questions,
        String user_name,
        String middle_name,
        String surname,

        String userName) {
    @JsonProperty("token")
    public String getTokenIdAsBase64() {
        String token = new String(this.token, StandardCharsets.UTF_8);

        log.info("getTokenIdAsBase64 token: {}, decoded:{}",token,  token);
        return token != null ? token : null;
    }

}

