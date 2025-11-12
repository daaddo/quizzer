package it.cascella.quizzer.repository;

import it.cascella.quizzer.dtos.UserQuizAttemptDto;
import it.cascella.quizzer.entities.UserQuizAttempt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface UserQuizAttemptRepository extends CrudRepository<UserQuizAttempt, Long> {

    @Modifying
    @Transactional
    @Query(
            value = """
            INSERT INTO user_quiz_attempt (user_id, token, attempted_at, status, user_name, surname, middle_name)
            VALUES (:userId, :tokenId, NOW(), 'IN_PROGRESS',:username, :surname, :middleName);
            """,
            nativeQuery = true
    )
    Integer insertIssuedQuiz(Integer userId,String tokenId, String username, String surname, String middleName);

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
    Integer updateUserQuizAttempt(Double score, LocalDateTime finishedAt, String questions, Integer userId, String issuedQuizId);

    @Query(
            value = """
            SELECT u.* FROM user_quiz_attempt u
            WHERE token = :token AND user_id = :userId
            """,
            nativeQuery = true
    )
    Optional<UserQuizAttempt> getByTokenAndUser_Id(String token, Integer userId);

    @Query(
            value = """
            SELECT u.*, u2.username
            FROM user_quiz_attempt u
            JOIN quizzer.users u2 on u.user_id = u2.id
            WHERE token = :token
            """,
            nativeQuery = true
    )
    List<UserQuizAttemptDto> getAllByToken(String token);

    @Modifying
    @Transactional
    @Query(
            value = """
            DELETE FROM user_quiz_attempt
            WHERE token = :token AND user_id = :userId
            """,
            nativeQuery = true
    )
    int deleteByTokenAndUserId(String token, Integer userId);
}
