package it.cascella.quizzer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class PublicQuizInfosId {

    @Column(name = "quiz_id")
    private Integer quizId;
}