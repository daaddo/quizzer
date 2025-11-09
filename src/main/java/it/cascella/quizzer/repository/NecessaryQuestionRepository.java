package it.cascella.quizzer.repository;


import it.cascella.quizzer.entities.NecessaryQuestion;
import it.cascella.quizzer.entities.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NecessaryQuestionRepository extends JpaRepository<NecessaryQuestion, Integer> {

    @Modifying
    @Transactional
    @Query(
            value = """
            INSERT INTO necessary_questions (id, issued_quiz_token)
            VALUES (:questionId, :token);
            """,
            nativeQuery = true
    )
    void saveNewNecessaryQuestion(String token, List<Integer> questionId);
}
