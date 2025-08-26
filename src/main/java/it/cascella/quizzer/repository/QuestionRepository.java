package it.cascella.quizzer.repository;

import it.cascella.quizzer.dtos.PutQuestionDTO;
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
        JOIN (SELECT * FROM users WHERE email LIKE :username OR username LIKE :username) user
        ON question.user_id = user.id
        ORDER BY RAND()
        LIMIT :size
"""
,
        nativeQuery = true
  )
  List<Question> findRandomQuestions(Integer size, String username);

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
            JOIN users u ON q.user_id = u.id
            WHERE q.id = :id AND (u.username = :username OR u.email = :username)
            """,
            nativeQuery = true
    )
    Optional<Question> findByIdAndUsername(Integer id, String username);


    @Query(
            value = """
            select q.* from question q
            join users u on q.user_id = u.id
            where q.id = :id and (u.username = :principal or u.email = :principal)
            """,
            nativeQuery = true
    )
    Optional<Object> findQuestionByIdAndPrincipal(Integer id, String principal);


    @Query(
            value = """
            SELECT q.*
            FROM question q
            JOIN users u ON q.user_id = u.id
            WHERE u.username = :principal OR u.email = :principal
            """,
            nativeQuery = true
    )
    List<Question> findAllFromPrincipal(String principal);


    @Query(
            value = """
            SELECT u.*
            FROM question q
            JOIN users u ON q.user_id = u.id
            WHERE q.quiz_id = :integer AND (u.username = :principal OR u.email = :principal)
            """,
            nativeQuery = true
    )
    Optional<Users> verifyQuizExistsInUser(Integer integer, String principal);

    @Modifying
    @Transactional
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