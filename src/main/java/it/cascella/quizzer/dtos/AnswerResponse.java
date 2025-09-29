package it.cascella.quizzer.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerResponse {
    private List<Integer> selectedOptions;
    private List<Integer> correctOptions;


}
