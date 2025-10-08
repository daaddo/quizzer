package it.cascella.quizzer.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.cascella.quizzer.entities.ProgressStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

@Slf4j
public record UserQuizAttemptDto(
        Integer id,
        Integer userId,
        byte[] token,
        Integer score,
        Timestamp attemptedAt,
        Timestamp finishedAt,
        String status,
        String questions,
        String user_name,
        String surname,
        String middle_name,
        String userName) {
    @JsonProperty("token")
    public String getTokenIdAsBase64() {
        String token = new String(this.token, StandardCharsets.UTF_8);

        log.info("getTokenIdAsBase64 token: {}, decoded:{}",token,  token);
        return token != null ? token : null;
    }

}

