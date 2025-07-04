package it.cascella.quizzer.repository;

import it.cascella.quizzer.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findByQuestionId(Integer questionId);
}