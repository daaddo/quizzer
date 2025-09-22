package it.cascella.quizzer.exceptions;

public class QuizzerException extends Exception {

    Integer code;

    public QuizzerException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
