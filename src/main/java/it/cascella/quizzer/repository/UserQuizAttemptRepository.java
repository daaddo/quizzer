package it.cascella.quizzer.repository;

import it.cascella.quizzer.entities.IssuedQuiz;
import it.cascella.quizzer.entities.UserQuizAttempt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserQuizAttemptRepository extends CrudRepository<UserQuizAttempt, Long> {

    @Modifying
    @Transactional
    @Query(
            value = """
            INSERT INTO user_quiz_attempt (user_id, token, attempted_at, status)
            VALUES (:userId, :tokenId, NOW(), 'IN_PROGRESS')
            """,
            nativeQuery = true
    )
    Integer insertIssuedQuiz(Integer userId,String tokenId);

    @Modifying
    @Transactional
    @Query(
            value = """
            update user_quiz_attempt
            set score = :score, finished_at = :finishedAt, questions = :questions, status = 'COMPLETED'
            where user_id = :userId and token = :issuedQuizId
            """,
            nativeQuery = true
    )
    Integer updateUserQuizAttempt(Integer score, String finishedAt, String questions, Integer userId, String issuedQuizId);

    @Query(
            value = """
            SELECT u.* FROM user_quiz_attempt u
            WHERE token = :token AND user_id = :userId
            """,
            nativeQuery = true
    )
    Optional<UserQuizAttempt> getByTokenAndUser_Id(String token, Integer userId);
}
