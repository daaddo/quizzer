package it.cascella.quizzer.dtos;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record NewQuizDTO(
        @NotNull(message = "Title cannot be null")
        @Length(min=3, max=30, message = "Title must be between 3 and 30 characters")
        String title,

        @NotNull(message = "Description cannot be null")
        @Length(min=10, max=300, message = "Description must be between 10 and 300 characters")
        String description,

        Boolean isPublic
)
{
}
