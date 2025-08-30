package it.cascella.quizzer.service;


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
}
