package it.cascella.quizzer.dtos;

public record UserInformationDTO(
        String username,
        List<QuizInformationDTO> quizzes
) {
}
