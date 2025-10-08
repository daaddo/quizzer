package it.cascella.quizzer.handler;

import it.cascella.quizzer.exceptions.QuizzerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Gestione personalizzata delle eccezioni di validazione
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // L'annotazione @ExceptionHandler indica che questo metodo
    //gestirà le eccezioni di tipo MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("=== VALIDATION EXCEPTION CAUGHT ===");
        log.error("MethodArgumentNotValidException: {}", ex.getMessage());
        log.error("Binding result: {}", ex.getBindingResult());
        log.error("Parameter: {}", ex.getParameter());
        log.error("Full stack trace: ", ex);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Bad Request");
        errors.put("status", "400");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            log.error("Validation error - Field: {}, Message: {}", fieldName, errorMessage);
            errors.put(fieldName, errorMessage);
        });
        errors.put("timestamp", timestamp);

        log.error("Returning validation errors: {}", errors);
        log.error("=== END VALIDATION EXCEPTION ===");
        return errors;
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        log.info("=== BAD CREDENTIALS EXCEPTION CAUGHT ===");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Unauthorized");
        errors.put("status", "401");
        errors.put("timestamp", timestamp);
        errors.put("message", "Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);

    }
    @ExceptionHandler(QuizzerException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(QuizzerException ex) {
        log.info("=== Quizzer EXCEPTION CAUGHT ===");
        log.info("Quizzer code: {}", ex.getCode());
        log.info("Quizzer message: {}", ex.getMessage());
        log.info("Quizzer cause: {}", ex.getCause() != null ? ex.getCause().getMessage() : "No cause");
        log.info("Full stack trace: ", ex);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        Map<String, String> errors = new HashMap<>();
        if (!(ex.getCode() >= 500)){
            log.error("INTERNAL SERVER ERROR, not exposing message FULL STACK TRACE {}", String.valueOf(ex));

        }
        errors.put("status", String.valueOf(ex.getCode()));
        errors.put("timestamp", timestamp);
        errors.put("HTTP", String.valueOf(HttpStatus.resolve(ex.getCode())));

        log.error("Returning Quizzer response: {}", errors);
        log.error("=== END Quizzer EXCEPTION ===");
        return ResponseEntity.status(ex.getCode()).body(errors);
    }

    // Gestione personalizzata delle eccezioni di validazione per le liste
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(HandlerMethodValidationException ex) {
        log.error("=== HANDLER METHOD VALIDATION EXCEPTION CAUGHT ===");
        log.error("HandlerMethodValidationException: {}", ex.getMessage());
        log.error("All errors: {}", ex.getAllErrors());
        log.error("Full stack trace: ", ex);

        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            log.error("Handler validation error - Field: {}, Message: {}", fieldName, message);
            errors.put(fieldName, message);
        });

        log.error("Returning handler validation errors: {}", errors);
        log.error("=== END HANDLER METHOD VALIDATION EXCEPTION ===");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("=== METHOD ARGUMENT TYPE MISMATCH EXCEPTION CAUGHT ===");
        log.error("MethodArgumentTypeMismatchException: {}", ex.getMessage());
        log.error("Parameter name: {}", ex.getName());
        log.error("Parameter value: {}", ex.getValue());
        log.error("Required type: {}", ex.getRequiredType());
        log.error("Full stack trace: ", ex);

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Bad Request");
        errors.put("status", "400");
        errors.put("timestamp", timestamp);
        errors.put("message", "Invalid value for parameter: " + ex.getName());

        log.error("Returning type mismatch errors: {}", errors);
        log.error("=== END METHOD ARGUMENT TYPE MISMATCH EXCEPTION ===");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String, String>> handleMethodTransactionSystemException(TransactionSystemException ex) throws Throwable {
        log.info("=== TransactionSystemException CAUGHT ===");
        if (ex.getOriginalException() instanceof QuizzerException exception){
            return handleValidationExceptions(exception);
        }

        return handleGenericException(ex);
    }

    // Add a catch-all exception handler to capture any unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("=== GENERIC EXCEPTION CAUGHT ===");
        log.error("Exception type: {}", ex.getClass().getSimpleName());
        log.error("Exception message: {}", ex.getMessage());
        log.error("Exception cause: {}", ex.getCause() != null ? ex.getCause().getMessage() : "No cause");
        log.error("Full stack trace: ", ex);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Internal Server Error");
        errors.put("status", "500");
        errors.put("timestamp", timestamp);
        errors.put("message", "An unexpected error occurred");
        errors.put("exceptionType", ex.getClass().getSimpleName());

        log.error("Returning generic exception response: {}", errors);
        log.error("=== END GENERIC EXCEPTION ===");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    // Puoi aggiungere altri metodi per gestire altre eccezioni qui
}
