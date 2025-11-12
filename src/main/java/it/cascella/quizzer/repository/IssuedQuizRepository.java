package it.cascella.quizzer.repository;

import it.cascella.quizzer.dtos.IssuedQuizInfosDto;
import it.cascella.quizzer.entities.IssuedQuiz;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IssuedQuizRepository extends CrudRepository<IssuedQuiz, Long> {


    @Modifying
    @Transactional
    @Query(
            value = """
            INSERT INTO issued_quiz (token_id, issuer_id, quiz_id, issued_at, expires_at, duration,number_of_questions, required_details)
            VALUES (:tokenId, :issuerId, :quizId, NOW(), :expire, :duration, :numberOfQuestions, :requiredDetails);
            """,
            nativeQuery = true
    )
    Integer insertIssuedQuiz(String tokenId, Integer issuerId, Integer quizId, LocalDateTime expire, Time duration, Integer numberOfQuestions, Boolean requiredDetails);

    Optional<IssuedQuiz> getByTokenId(String tokenId);


    @Query(
            value = """
            SELECT iq.*
            FROM issued_quiz iq
            WHERE iq.issuer_id = :id
            ORDER BY iq.issued_at DESC
            """,
            nativeQuery = true
    )
    List<IssuedQuizInfosDto> getIssuedQuiz(Integer id, Integer quizId);

    @Query(
            value = """
            SELECT iq.number_of_questions , iq.expires_at , iq.duration, iq.required_details 
            FROM issued_quiz iq
            WHERE iq.token_id = :token
            ORDER BY iq.issued_at DESC
            LIMIT 1
            """,
            nativeQuery = true
    )
    Optional<Object[]> getIssuedQuizInfosForUser(String token);

    @Modifying
    @Transactional
    @Query(
            value = """
            UPDATE issued_quiz
            SET expires_at = :expire
            WHERE token_id = :token
            """,
            nativeQuery = true
    )
    int updateExpiration(String token, LocalDateTime expire);

    @Modifying
    @Transactional
    @Query(
            value = """
            UPDATE issued_quiz
            SET number_of_questions = :numberOfQuestions
            WHERE token_id = :token
            """,
            nativeQuery = true
    )
    int updateNumberOfQuestions(String token, Integer numberOfQuestions);

    @Modifying
    @Transactional
    @Query(
            value = """
            DELETE FROM issued_quiz
            WHERE token_id = :token
            """,
            nativeQuery = true
    )
    int deleteByToken(String token);

    @Query(
            value = """
            SELECT iq.required_details
            FROM issued_quiz iq
            WHERE iq.token_id = :tokenId
            """,
            nativeQuery = true
    )
    Optional<Boolean> isAdditionalInformationRequired(String tokenId);

}
