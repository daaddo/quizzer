package it.cascella.quizzer.repository;


import it.cascella.quizzer.entities.NecessaryQuestion;
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

    @Query(value = """
            SELECT *
            FROM ((
                SELECT q.id, q.title, q.question, if(q.is_multiple_choice= 0, false, true) AS is_multiple_choice
                FROM necessary_questions AS tokenized
                JOIN question q ON q.id = tokenized.id
                WHERE tokenized.issued_quiz_token = :idIssuedQuizToken
            )
            UNION ALL
            (
              SELECT q.id, q.title, q.question, if(q.is_multiple_choice= 0, false, true) AS is_multiple_choice
              FROM necessary_questions AS tokenized
              RIGHT JOIN question q ON q.id = tokenized.id
              LEFT JOIN issued_quiz iq on q.quiz_id = iq.quiz_id
              WHERE tokenized.id IS NULL AND iq.token_id= :idIssuedQuizToken
              ORDER BY RAND()
            )
            LIMIT :size) AS combined_results
            JOIN answer on combined_results.id = answer.question_id;
            """,nativeQuery = true)
    List<Object> getAllById_IssuedQuizToken(String idIssuedQuizToken,Integer size);
}
