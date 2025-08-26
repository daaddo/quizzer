package it.cascella.quizzer.dtos;

public record QuizInformationDTO(
        Integer id,
        String title,
        String description,
        Integer questionCount
) {
}
