package it.cascella.quizzer.dtos;

public record PutQuizDTO(
    Integer id,
    String title,
    String description
) {
}
