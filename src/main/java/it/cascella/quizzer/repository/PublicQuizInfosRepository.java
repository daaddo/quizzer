package it.cascella.quizzer.repository;

import it.cascella.quizzer.entities.PublicQuizInfos;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PublicQuizInfosRepository extends CrudRepository<PublicQuizInfos, Integer> {
    void deleteByQuiz_Id(Integer quizId);


    @NativeQuery(
            """
        INSERT INTO public_quiz_infos(quiz_id)
        VALUES (:id)
"""
    )
    @Modifying
    @Transactional
    void insertNewPublic(Integer id);
}