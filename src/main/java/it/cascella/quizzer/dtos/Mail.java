package it.cascella.quizzer.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mail {
    private String to;
    private String subject;
    private String body;
}
