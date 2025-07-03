package it.cascella.quizzer.dtos;

public record CreateAnswerDto(
        String answer,
        Boolean correct
) {
}
