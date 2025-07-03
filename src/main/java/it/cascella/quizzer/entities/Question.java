package it.cascella.quizzer.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String question;

    @OneToMany
    @JoinColumn(name = "question_id")
    private List<Answer> answers;


}
