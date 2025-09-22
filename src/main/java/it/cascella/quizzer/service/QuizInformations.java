package it.cascella.quizzer.service;

import it.cascella.quizzer.entities.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuizInformations {
    private CustomUserDetails owner;
    private Integer quizId;
    private String title;
    private String description;
    private Integer numberOfQuestions;
    private Integer durationInMinutes;
    private List<QuizUserInformation> usersTakingTheQuiz;
}
