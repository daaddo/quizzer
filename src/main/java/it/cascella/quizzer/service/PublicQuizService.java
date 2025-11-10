package it.cascella.quizzer.service;

import it.cascella.quizzer.dtos.PublicQuizDto;
import it.cascella.quizzer.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PublicQuizService {
    private final QuizRepository quizRepository;

    @Autowired
    public PublicQuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }


    public Page<PublicQuizDto> getAllPaged(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return quizRepository.findAllPublicQuizzesPaged(pageable.getSort().toString(),pageable);
    }
}
