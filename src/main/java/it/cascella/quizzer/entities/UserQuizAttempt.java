package it.cascella.quizzer.entities;

import it.cascella.quizzer.dtos.AnswerResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_quiz_attempt")
public class UserQuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "token", nullable = false)
    private IssuedQuiz token;

    @NotNull
    @Column(name = "score", nullable = false)
    private Integer score;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "attempted_at")
    private LocalDateTime attemptedAt;
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "questions", nullable = false)
    private Map<Integer, AnswerResponse> questions;
    @NotNull
    @ColumnDefault("'IN_PROGRESS'")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProgressStatus status;


}
