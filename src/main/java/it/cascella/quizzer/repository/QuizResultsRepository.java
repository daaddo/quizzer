package it.cascella.quizzer.repository;

import it.cascella.quizzer.controller.QuizController;
import it.cascella.quizzer.dtos.GetQuestionDto;
import it.cascella.quizzer.dtos.QuizInfosResponse;
import it.cascella.quizzer.entities.QuizResults;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizResultsRepository extends CrudRepository<QuizResults, Integer> {

    @Modifying
    @Transactional
    @NativeQuery(
            """
        INSERT INTO quiz_results (quiz_id, user_id, score, results,token)
        VALUES (:quizId, :userId, :score, CAST(:results AS JSON), :token)
"""
    )
    void saveNewQuizInfos(
            @Param("quizId") Integer quizId,
            @Param("userId") Integer userId,
            @Param("score") Double score,
            @Param("results") String resultsJson,
            @Param("token") String token
    );



    @NativeQuery(
            """
        SELECT 
        qr.quiz_id AS quizResultId,
        q.title AS quizTitle,
        q.description AS quizDescription,
        qr.score AS score, 
        qr.token AS token,
        qr.results as results,
        qr.taken_at AS takenAt
        FROM quiz_results qr
        JOIN quiz q ON qr.quiz_id = q.id
        WHERE qr.user_id = :userId
        ORDER BY qr.taken_at DESC
                             """
    )
    List<QuizInfosResponse> getAllByUser_Id(Integer userId);
}
