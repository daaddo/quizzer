package it.cascella.quizzer.entities;

import it.cascella.quizzer.dtos.PublicQuizDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@SqlResultSetMapping(
        name = "PublicQuizDtoMapping",
        classes = @ConstructorResult(
                targetClass = PublicQuizDto.class,
                columns = {
                        @ColumnResult(name = "quiz_id", type = Integer.class),
                        @ColumnResult(name = "review_count", type = Integer.class),
                        @ColumnResult(name = "average_rating", type = Double.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "questions_count", type = Integer.class),
                        @ColumnResult(name = "last_updated", type = LocalDateTime.class)
                }
        )
)
@Getter
@Setter
@Entity
@Table(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "questions_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer questionCount = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;


    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

}