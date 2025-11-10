package it.cascella.quizzer.controller;


import it.cascella.quizzer.dtos.PublicQuizDto;
import it.cascella.quizzer.service.PublicQuizService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/publicquizz")
public class PublicQuizController {


    private final PublicQuizService publicQuizService;

    public PublicQuizController(PublicQuizService publicQuizService) {
        this.publicQuizService = publicQuizService;
    }

    @GetMapping()
    public ResponseEntity<Page<PublicQuizDto>> getAllPublicQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "quiz_id") String sortBy) {


        return ResponseEntity.ok(publicQuizService.getAllPaged(page, size, sortBy));
        // Implementation to fetch and return all public quizzes
    }
}
