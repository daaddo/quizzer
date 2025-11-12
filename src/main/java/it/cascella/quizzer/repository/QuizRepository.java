package it.cascella.quizzer.repository;

import it.cascella.quizzer.dtos.PublicQuizDto;
import it.cascella.quizzer.entities.Quiz;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
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

    Optional<Quiz> getByIdAndUserId_Id(Integer id, Integer userIdId);

    Optional<Quiz> getById(Integer id);




    @NativeQuery(
            value = """
            SELECT              pqi.quiz_id,
                              pqi.review_count,
                              pqi.average_rating,
                              q.title,
                              q.description,
                              q.questions_count,
                              pqi.last_updated,
                              u.username as author_username
            FROM public_quiz_infos pqi
            JOIN quiz q ON pqi.quiz_id = q.id
            JOIN quizzer.users u on u.id = q.user_id
            """,
            sqlResultSetMapping = "PublicQuizDtoMapping",
            countQuery = """
            SELECT COUNT(*)
            FROM public_quiz_infos pq
"""
    )
    Page<PublicQuizDto> findAllPublicQuizzesPaged(String sortCol, Pageable pageable);
}