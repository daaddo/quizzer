package it.cascella.quizzer.entities;


import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
