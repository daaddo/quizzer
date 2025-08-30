package it.cascella.quizzer.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateQuestionDto(
        String title,
        String question,

        @NotNull
        Integer quizId,
        @Size(min=1,max=10,message = "")
        List<CreateAnswerDto> answers
) {

}
