package it.cascella.quizzer.repository;

import it.cascella.quizzer.dtos.QuizInformationDTO;
import it.cascella.quizzer.entities.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {


    /**
     * Finds users by their username or email.
     *
     * @param usernameOrEmail the username or email to search for
     * @return a list of users matching the given username or email
     */

    @Query(
            value = "SELECT u.* FROM Users u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail",
            nativeQuery = true
    )
    List<Users> findByUsernameOrEmail(String usernameOrEmail);



    @Query(
            value = """
                    SELECT q.id AS id, q.title AS title, q.description AS description, q.questions_count AS questionCount
                    FROM quiz q
                    WHERE q.user_id = :id
                    """,
            nativeQuery = true
    )
    List<QuizInformationDTO> getQuizInformationsByUserId(Integer id);

    Users getUsersById(Integer id);

    boolean existsUsersByUsernameOrEmail(@NotBlank String username, @NotBlank String email);

    Optional<Users> findUsersByEmail(String email);
}
