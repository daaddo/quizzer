package it.cascella.quizzer.dtos;

import java.util.List;

public record CreateQuestionDto(
        String title,
        String question,
        Integer quizId,
        List<CreateAnswerDto> answers
) {

}
