package it.cascella.quizzer.repository;

import it.cascella.quizzer.entities.Quiz;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuizRepository extends CrudRepository<Quiz, Integer> {
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

    @Modifying
    @Transactional
    @Query(
            value = """
            insert into quiz (title, description, user_id)
            values (:title, :description, :id)
            """,
            nativeQuery = true
    )

    Integer insertQuiz(String title, String description, Integer id);


    @Modifying
    @Transactional
    @Query(
            value = """
            update quiz
            set title = :title,
                description = :description
            where id = :id1 and user_id = :id
            """,
            nativeQuery = true
    )
    Integer updateQuiz(String title, String description, Integer id, Integer id1);

    Optional<Quiz> findByIdAndUserId_Id(Integer quizId, Integer id);
}