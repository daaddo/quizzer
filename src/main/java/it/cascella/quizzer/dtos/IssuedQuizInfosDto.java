package it.cascella.quizzer.dtos;

import java.sql.Time;
import java.sql.Timestamp;

public record IssuedQuizInfosDto(
        byte[] tokenId,
        Integer issuerId,
        Integer quizId,
        Integer numberOfQuestions,
        Timestamp issuedAt,
        Timestamp expiresAt,
        Time duration
) {}
