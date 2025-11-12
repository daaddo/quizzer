package it.cascella.quizzer.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public record QuizInfosResponse(
    Integer quizResultId,
    String quizTitle,
    String quizDescription,
    Double score,
    @JsonIgnore byte[] token,
    String results,
    Timestamp takenAt
) {
    @JsonProperty("token")
    public String getTokenAsString() {
        if (token == null) {
            return "";
        }
        return new String(token);
    }
}
