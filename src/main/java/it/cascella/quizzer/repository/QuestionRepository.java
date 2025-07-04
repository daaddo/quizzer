package it.cascella.quizzer.repository;

import it.cascella.quizzer.dtos.PutQuestionDTO;
import it.cascella.quizzer.entities.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {


  @Query(
        value = "SELECT * FROM question ORDER BY RAND() LIMIT :size",
        nativeQuery = true
  )
  List<Question> findRandomQuestions(Integer size);

  @Modifying
  @Transactional
  @Query(
          value="DELETE FROM question WHERE id = :id",
            nativeQuery = true
  )
  void deleteQuestionById(Integer id);

  @Modifying
    @Transactional
  @Query(
         value= """
        update question q
        set q.title = :title,
            q.question = :question
        where q.id = :id
        """,
            nativeQuery = true
  )

  Integer updateQuestion(String title, String question, Integer id);
}