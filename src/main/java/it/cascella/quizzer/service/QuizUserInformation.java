package it.cascella.quizzer.service;

import it.cascella.quizzer.entities.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@AllArgsConstructor
@Getter
@Setter
public class QuizUserInformation {
    private Status status;
    private Integer score;
    private Date startedAt;
    private Date completedAt;

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

}
