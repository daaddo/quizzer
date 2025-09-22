package it.cascella.quizzer.dtos;

import java.util.List;

public record GetQuestionDtoNotCorrected(Integer quizId,
                                         String title,
                                         String question,
                                         List<GetAnswerDtoNotCorrected> list,
                                         Boolean multipleChoice
)
{
}
