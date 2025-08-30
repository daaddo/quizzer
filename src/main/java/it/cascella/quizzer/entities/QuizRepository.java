package it.cascella.quizzer.entities;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface QuizRepository extends Repository<Quiz, Integer> {
    Quiz findQuizById(Integer id);

    @Modifying
    @Transactional
    @Query(
            value = """
            update quiz q
            set q.questions_count = q.questions_count + 1
            where q.id = :integer
            """,
            nativeQuery = true
    )
    void incrementQuestionCount(Integer integer);

    @Modifying
    @Transactional
    @Query(
            value = """
            update quiz q
                
            set q.questions_count = q.questions_count - 1
            where q.id = :id
            """,
            nativeQuery = true
    )
    void decrementQuestionCount(Integer id);

    @Modifying
    @Transactional
    Integer deleteQuizByIdAndUserId_Id(Integer id, Integer userIdId);
}