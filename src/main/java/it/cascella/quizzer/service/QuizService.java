package it.cascella.quizzer.service;


import it.cascella.quizzer.dtos.NewQuizDTO;
import it.cascella.quizzer.dtos.PutQuizDTO;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.entities.Quiz;
import it.cascella.quizzer.entities.QuizRepository;
import it.cascella.quizzer.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Modifying
    public void deleteQuiz(Integer id, Integer userId) {
        Integer i = quizRepository.deleteQuizByIdAndUserId_Id(id, userId);
        if (i != 1) {
            throw new RuntimeException("Quiz not found with id: " + id + " for user: " + userId);
        }
    }
    @Transactional
    @Modifying
    public Integer insertQuiz(NewQuizDTO newQuiz, Integer id) {
        Quiz quiz = new Quiz();
        quiz.setDescription( newQuiz.description());
        quiz.setTitle(newQuiz.title());

        quiz.setUserId(userRepository.getUsersById(id));
        quizRepository.save(quiz);
        return quiz.getId();
    }

    @Transactional
    @Modifying
    public void updateQuiz(PutQuizDTO newQuiz, Integer id) {

        Integer i = quizRepository.updateQuiz(newQuiz.title(), newQuiz.description(), newQuiz.id(), id);

        if (i != 1) {
            throw new RuntimeException("Quiz not found with id: " + newQuiz.id() + " for user: " + id);
        }
    }

    public String generateLink(Integer quizId, Integer durationInMinutes, Integer id, CustomUserDetails details) {
        return null;
    }
}
