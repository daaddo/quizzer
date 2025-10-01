package it.cascella.quizzer.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class AnswerResponse {
    @NotNull
    private List<Integer> selectedOptions = new LinkedList<>();
    @NotNull
    private List<Integer> correctOptions = new LinkedList<>();

    @Override
    public String toString() {
        return "AnswerResponse{" +
                "selectedOptions=" + selectedOptions +
                ", correctOptions=" + correctOptions +
                '}';
    }
}
