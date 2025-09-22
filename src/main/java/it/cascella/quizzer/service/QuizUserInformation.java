package it.cascella.quizzer.service;

import it.cascella.quizzer.entities.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class QuizUserInformation {
    private CustomUserDetails details;
    private Status status;
    private Integer score;

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

}
