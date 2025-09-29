package it.cascella.quizzer.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
public class IssuedQuiz {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issuer_id", nullable = false)
    private Users issuer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "issued_at")
    private Instant issuedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "duration")
    private Instant duration;

    public Users getIssuer() {
        return issuer;
    }

    public void setIssuer(Users issuer) {
        this.issuer = issuer;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getDuration() {
        return duration;
    }

    public void setDuration(Instant duration) {
        this.duration = duration;
    }
}
