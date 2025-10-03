package it.cascella.quizzer.dtos;

import java.util.List;

public record UserInformationDTO(
        Integer id,
        String username,
        String email,
        List<QuizInformationDTO> quizzes
) {
}
