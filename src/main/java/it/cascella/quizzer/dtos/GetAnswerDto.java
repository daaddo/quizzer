package it.cascella.quizzer.dtos;

public record GetAnswerDto(
        Integer id,
        String answer,
        Boolean correct
) {
}
