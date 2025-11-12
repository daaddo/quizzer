package it.cascella.quizzer.dtos;

public record QuizInfosResponse(
    Integer quizResultId,
    String quizTitle,
    String quizDescription,
    Double score,
    String token,
    String results,
    String takenAt
) {
}
