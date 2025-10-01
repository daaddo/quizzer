package it.cascella.quizzer.dtos;

import it.cascella.quizzer.entities.ProgressStatus;

import java.sql.Timestamp;

public record UserQuizAttemptDto(
        Integer id,
        Integer userId,
        byte[] token,
        Integer score,
        Timestamp attemptedAt,
        Timestamp finishedAt,
        ProgressStatus status,
        String questions,
        String username
) {}

