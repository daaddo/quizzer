package it.cascella.quizzer.dtos;

import jakarta.validation.constraints.NotNull;

public record PutQuestionDTO(
        @NotNull
        Integer id,
        String title,
        String question
) {
}