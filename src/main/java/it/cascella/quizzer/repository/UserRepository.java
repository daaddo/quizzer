package it.cascella.quizzer.repository;

import it.cascella.quizzer.entities.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

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
}
