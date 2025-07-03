package it.cascella.quizzer.entities;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false, name = "is_correct")
    private Boolean correct;

    @OneToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
