package it.cascella.quizzer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ReviewId implements Serializable {
    @NotNull
    @Column(name = "quiz_id", nullable = false)
    private Integer quizId;

    @NotNull
    @Column(name = "reviewer_id", nullable = false)
    private Integer reviewerId;


}