package it.cascella.quizzer.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Table(name = "necessary_questions")
@Data
public class NecessaryQuestion {

    @EmbeddedId
    private NecessaryQuestionId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("id") // collega questo campo al campo 'id' nella chiave
    @JoinColumn(name = "id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("issuedQuizToken") // collega questo campo al campo 'issuedQuizToken' nella chiave
    @JoinColumn(name = "issued_quiz_token", nullable = false)
    private IssuedQuiz issuedQuiz;


}
