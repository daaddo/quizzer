package it.cascella.quizzer.dtos;

public record PutQuestionDTO(
        Integer id,
        String title,
        String question
) {
}