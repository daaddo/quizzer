package it.cascella.quizzer.entities;

import org.springframework.data.repository.Repository;

public interface QuizRepository extends Repository<Quiz, Integer> {
    Quiz findQuizById(Integer id);
}