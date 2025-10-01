package it.cascella.quizzer.dtos;

import java.sql.Time;
import java.time.Instant;

public record QuizInfos(
    Integer numberOfQuestions,
    Instant expirationDate,
    Time duration
) {
}
