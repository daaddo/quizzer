package it.cascella.quizzer.repository;

import it.cascella.quizzer.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {


  @Query(
        value = "SELECT * FROM question ORDER BY RANDOM() LIMIT :size",
        nativeQuery = true
  )
  List<Question> findRandomQuestions(Integer size);
}