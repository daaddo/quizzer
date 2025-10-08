package it.cascella.quizzer.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record QuizInfos(
    Integer numberOfQuestions,
    LocalDateTime expirationDate,
    LocalTime duration,
    Boolean required_details
) {
    public QuizInfos (Object[] fromSQL){
        this(
                fromSQL[0] != null ? (Integer) fromSQL[0] : null,
                fromSQL[1] != null ? ((java.sql.Timestamp) fromSQL[1]).toLocalDateTime() : null,
                fromSQL[2] != null ? ((java.sql.Time) fromSQL[2]).toLocalTime() : null,
                fromSQL[3] != null ? (Boolean) fromSQL[3] : null
        );
    }
}
