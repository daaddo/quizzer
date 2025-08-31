package it.cascella.quizzer.repository;

import it.cascella.quizzer.entities.Question;
import it.cascella.quizzer.entities.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {


  @Query(
        value = """
        SELECT question.*
        FROM question
        WHERE user_id = :userId and quiz_id = :quizId
        ORDER BY RAND()
        LIMIT :size
"""
,
        nativeQuery = true
  )
  List<Question> findRandomQuestions(Integer size,Integer quizId, Integer userId);

  @Modifying
  @Transactional
  @Query(
          value="DELETE FROM question WHERE id = :id",
            nativeQuery = true
  )
  void deleteQuestionById(Integer id);

  @Modifying
    @Transactional
  @Query(
         value= """
        update question q
        set q.title = :title,
            q.question = :question
        where q.id = :id
        """,
            nativeQuery = true
  )

  Integer updateQuestion(String title, String question, Integer id);


    @Query(
            value = """
            SELECT q.*
            FROM question q
            WHERE q.id = :id AND q.user_id = :userId
            """,
            nativeQuery = true
    )
    Optional<Question> findByIdAndUsername(Integer id, Integer userId);


    @Query(
            value = """
            select q.* from question q
            where q.id = :id and q.user_id = :principal
            """,
            nativeQuery = true
    )
    Optional<Object> findQuestionByIdAndPrincipal(Integer id, Integer principal);


    @Query(
            value = """
            SELECT q.*
            FROM (select q.* from question q where q.quiz_id = :quizId) q
            WHERE q.user_id = :principal
            """,
            nativeQuery = true
    )
    List<Question> findAllFromPrincipal(Integer principal, Integer quizId);


    @Query(
            value = """
            SELECT u.*
            FROM quiz q
            JOIN users u ON q.user_id = u.id
            WHERE q.id = :integer AND u.id = :principal
            """,
            nativeQuery = true
    )
    Optional<Users> verifyQuizExistsInUser(Integer integer, Integer principal);

    @Modifying
    @Transactional
    @Deprecated
    @Query(

            value = """
            INSERT INTO question (quiz_id, user_id)
            SELECT :integer, u.id
            FROM users u
            WHERE u.username = :principal OR u.email = :principal
            """,
            nativeQuery = true
    )
    void saveQuestion(Integer integer, String principal);
}