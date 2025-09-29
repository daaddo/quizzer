package it.cascella.quizzer.repository;

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
            INSERT INTO issued_quiz (token_id, issuer_id, quiz_id, issued_at, expires_at, duration,number_of_questions)
            VALUES (:tokenId, :issuerId, :quizId, NOW(), :expire, :duration, :numberOfQuestions)
            """,
            nativeQuery = true
    )
    Integer insertIssuedQuiz(String tokenId, Integer issuerId, Integer quizId, LocalDateTime expire, Time duration, Integer numberOfQuestions);

    Optional<IssuedQuiz> getByTokenId(String tokenId);
}
