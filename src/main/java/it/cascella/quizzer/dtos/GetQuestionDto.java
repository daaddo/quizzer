package it.cascella.quizzer.dtos;

import java.util.List;

public record GetQuestionDto(
        Integer id,
        String title,
        String question,
        List<GetAnswerDto> answers
) {
}
