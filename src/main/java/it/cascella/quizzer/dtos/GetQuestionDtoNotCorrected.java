package it.cascella.quizzer.dtos;

import java.util.List;

public record GetQuestionDtoNotCorrected(Integer id, String title, String question,
                                         List<GetAnswerDtoNotCorrected> list,Boolean multipleChoice) {
}
