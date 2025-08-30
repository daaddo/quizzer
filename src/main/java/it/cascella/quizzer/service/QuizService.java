package it.cascella.quizzer.service;


import it.cascella.quizzer.dtos.NewQuizDTO;
import it.cascella.quizzer.dtos.PutQuizDTO;
import it.cascella.quizzer.entities.QuizRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Transactional
    @Modifying
    public void deleteQuiz(Integer id, Integer userId) {
        Integer i = quizRepository.deleteQuizByIdAndUserId_Id(id, userId);
        if (i != 1) {
            throw new RuntimeException("Quiz not found with id: " + id + " for user: " + userId);
        }
    }

    public Integer insertQuiz(NewQuizDTO newQuiz, Integer id) {
        return quizRepository.insertQuiz(newQuiz.title(), newQuiz.description(), id);
    }


    public void updateQuiz(PutQuizDTO newQuiz, Integer id) {

        Integer i = quizRepository.updateQuiz(newQuiz.title(), newQuiz.description(), newQuiz.id(), id);

        if (i != 1) {
            throw new RuntimeException("Quiz not found with id: " + newQuiz.id() + " for user: " + id);
        }
    }
}
