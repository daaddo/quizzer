package it.cascella.quizzer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PublicQuizInfosId implements Serializable {

    @NotNull
    @Column(name = "quiz_id")
    private Integer quizId;

}