package it.cascella.quizzer.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import it.cascella.quizzer.dtos.*;
import it.cascella.quizzer.entities.CustomUserDetails;
import it.cascella.quizzer.exceptions.QuizzerException;
import it.cascella.quizzer.service.QuizService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {
    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Integer id,  @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("Deleting quiz with id {}for user {}" ,id, principal.getUsername());
        quizService.deleteQuiz(id,principal.getId());
    }

    @PostMapping
    public ResponseEntity<Integer> insertNewQuiz(@RequestBody NewQuizDTO newQuiz, @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("inserting quiz for user {}",  principal.getUsername());
        return ResponseEntity.ok().body(quizService.insertQuiz(newQuiz,principal.getId()));
    }

    @PutMapping
    public void updateQuiz(@RequestBody PutQuizDTO newQuiz, @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("updating quiz for user {}",  principal.getUsername());
        quizService.updateQuiz(newQuiz,principal.getId());
    }


    @GetMapping("/doesRequireDetails")
    public ResponseEntity<Boolean> doesRequireDetails(@RequestParam String token)
throws QuizzerException {
        log.info("Checking if quiz requires details for token: {}", token);
        return ResponseEntity.ok(quizService.doesRequireDetails(token));
    }
    record  LinkRequest(
            Integer quizId,
            Integer numberOfQuestions,
            Time duration,
            LocalDateTime expirationDate,
            Boolean requiredDetails,
            List<Integer> requiredQuestions
    ) {}
    @PostMapping("/link")
    public ResponseEntity<String> generateLink(@RequestBody LinkRequest params, @AuthenticationPrincipal CustomUserDetails principal) throws QuizzerException {
        log.info("generating link for quiz {} for user {}",  params.quizId, principal.getUsername());
        String token= quizService.generateLink(
                params.quizId,
                params.numberOfQuestions(),
                params.duration,
                params.expirationDate,
                params.requiredDetails,
                params.requiredQuestions,
                principal);
        return ResponseEntity.ok(token);
    }



    public record QuizInfosDto(
            Integer quizId,
            String title,
            String description,
            List<DomandaDTO> domande
    ) {}
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DomandaDTO {
        private String titolo;
        private String descrizione;
        private List<RispostaDTO> risposte;
    }

    public record RispostaDTO(
            String testo,
            boolean corretta,
            boolean chosen
    ) {}
    @PostMapping("/postPrivateAnswers")
    public ResponseEntity<String> submitAnswersInQuiz(@RequestBody QuizInfosDto submitAnswers,
                                                      @AuthenticationPrincipal CustomUserDetails principal) throws QuizzerException {
        log.info("Submitting answers for quiz {} by user {}", submitAnswers.quizId, principal.getUsername());
        quizService.submitAnswersInQuiz(
                submitAnswers,
                principal);
        return ResponseEntity.ok("Answers submitted successfully");
    }
    @GetMapping("/getPrivateAnswers")
    public ResponseEntity<List<QuizInfosResponse>> getAnswersInQuiz(@AuthenticationPrincipal CustomUserDetails principal) throws QuizzerException {
        log.info("Fetching answers for user {}", principal.getUsername());
        return ResponseEntity.ok(quizService.getQuizResults(principal));
    }

    @PostMapping("/random")
    public ResponseEntity<HashMap<QuizInfos,List<GetQuestionDtoNotCorrected>>> getRandomSetOfQuestions
            (@RequestParam String token,
             @AuthenticationPrincipal CustomUserDetails principal,
             @RequestBody(required = false) AdditionalInfoDTO additionalInfoDTO
    ) throws QuizzerException {
        log.info("Fetching a random set of questions from token: {}", token);
        log.info("Fetching for {}", principal);
        return ResponseEntity.ok(quizService.getQuestionFromToken(token,principal, additionalInfoDTO));
    }




    @PostMapping("/postAnswers")
    public ResponseEntity<HashMap<Integer, AnswerResponse>> submitAnswers(
            @RequestParam String token,
            @RequestBody HashMap<Integer,List<Integer>> answers,
            @AuthenticationPrincipal CustomUserDetails principal) throws QuizzerException, JsonProcessingException {
        log.info("Submitting answers for token: {}", token);
        HashMap<Integer, AnswerResponse> answersByUser = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> integerListEntry : answers.entrySet()) {
            answersByUser.put(integerListEntry.getKey(), new AnswerResponse(integerListEntry.getValue(), new LinkedList<>()));
        }
        return ResponseEntity.ok(quizService.submitAnswers(token, answersByUser, principal));
    }

    // --- New endpoint: get questions by token and payload ---
    /**
     * Dato un token e un JSON con mappa questionId -> AnswerResponse, valida issuer
     * e restituisce la lista delle domande complete per gli id forniti.
     * Endpoint: Post /api/v1/quizzes/questions-by-token
     * Esempio payload:
     * {
     *   "token": "...",
     *   "questions": {"3": {"correctOptions": [7], "selectedOptions": []}, ...}
     * }
     */
    public record QuestionsByTokenRequest(
            String token,
            Integer user_id
    ) {}

    @PostMapping("/questions-by-token")
    public ResponseEntity<List<Object>> getQuestionsByToken(@RequestBody QuestionsByTokenRequest request,
                                                                   @AuthenticationPrincipal CustomUserDetails principal) throws QuizzerException {
        return ResponseEntity.ok(
                quizService.getQuestionsForToken(request, principal)
        );
    }

    // --- New endpoints for IssuedQuiz and Attempts management ---
    record UpdateExpirationRequest(
            String token,
            LocalDateTime expirationDate
    ) {}

    record UpdateNumberOfQuestionsRequest(
            String token,
            Integer numberOfQuestions
    ) {}

    /**
     * Elimina il tentativo di un utente associato ad un issued quiz.
     *
     * Endpoint: DELETE /api/v1/quizzes/attempt
     * Richiede che l'utente autenticato sia l'issuer del token.
     *
     * @param token    token dell'issued quiz
     * @param userId   id dell'utente di cui eliminare il tentativo
     * @param principal utente autenticato
     * @throws QuizzerException se token non valido, issuer non autorizzato o tentativo non trovato
     */
    @DeleteMapping("/attempt")
    public void deleteAttempt(@RequestParam String token, @RequestParam Integer userId, @AuthenticationPrincipal CustomUserDetails principal) throws QuizzerException {
        log.info("Deleting attempt for token {} and user {} by issuer {}", token, userId, principal.getUsername());
        quizService.deleteUserAttempt(token, userId, principal);
    }

    /**
     * Aggiorna la data/ora di scadenza di un issued quiz.
     *
     * Endpoint: PUT /api/v1/quizzes/issued/expiration
     *
     * @param request   payload contenente token ed expiresAt (ISO-8601)
     * @param principal utente autenticato (deve essere l'issuer)
     * @throws QuizzerException se token non valido o issuer non autorizzato
     */
    @PutMapping("/issued/expiration")
    public void updateIssuedExpiration(@RequestBody UpdateExpirationRequest request, @AuthenticationPrincipal CustomUserDetails principal) throws QuizzerException {
        log.info("Updating expiration for token {} to {} by issuer {}", request.token(), request.expirationDate(), principal.getUsername());
        quizService.updateIssuedQuizExpiration(request.token(), request.expirationDate(), principal);
    }

    /**
     * Aggiorna il numero di domande assegnate all'issued quiz.
     *
     * Endpoint: PUT /api/v1/quizzes/issued/number-of-questions
     *
     * @param request   payload contenente token e numberOfQuestions (> 0)
     * @param principal utente autenticato (deve essere l'issuer)
     * @throws QuizzerException se token non valido, issuer non autorizzato o numero non valido
     */
    @PutMapping("/issued/number-of-questions")
    public void updateIssuedNumberOfQuestions(@RequestBody UpdateNumberOfQuestionsRequest request, @AuthenticationPrincipal CustomUserDetails principal) throws QuizzerException {
        log.info("Updating number of questions for token {} to {} by issuer {}", request.token(), request.numberOfQuestions(), principal.getUsername());
        quizService.updateIssuedQuizNumberOfQuestions(request.token(), request.numberOfQuestions(), principal);
    }

    /**
     * Elimina un issued quiz (e i tentativi correlati tramite ON DELETE CASCADE).
     *
     * Endpoint: DELETE /api/v1/quizzes/issued
     *
     * @param token     token dell'issued quiz da eliminare
     * @param principal utente autenticato (deve essere l'issuer)
     * @throws QuizzerException se token non valido o issuer non autorizzato
     */
    @DeleteMapping("/issued")
    public void deleteIssued(@RequestParam String token, @AuthenticationPrincipal CustomUserDetails principal) throws QuizzerException {
        log.info("Deleting issued quiz {} by issuer {}", token, principal.getUsername());
        quizService.deleteIssuedQuiz(token, principal);
    }
}
