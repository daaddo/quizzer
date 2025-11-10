package it.cascella.quizzer.dtos;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;

import java.time.LocalDateTime;



public record PublicQuizDto(
    Integer quizId,
    Integer reviewCount,
    Double averageRating,
    String title,
    String description,
    Integer questionsCount,
    LocalDateTime lastUpdated
) {

}
