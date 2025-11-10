package it.cascella.quizzer.repository;

import it.cascella.quizzer.entities.PublicQuizInfos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PublicQuizInfosRepository extends CrudRepository<PublicQuizInfos, Integer> {
    void deleteByQuiz_Id(Integer quizId);
}