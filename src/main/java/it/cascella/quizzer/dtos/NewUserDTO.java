package it.cascella.quizzer.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotBlank
        @Size(min = 3, max = 20, message = "Lo Username deve essere tra 3 e 20) caratteri")
        String username,
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "La password deve contenere almeno 1 minuscola, 1 maiuscola e 1 numero"
        )
        @Size(min = 8, message = "La password deve essere di almeno 8 caratteri")
        String password,
        @NotBlank(message = "La Email è obbligatoria")
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "La Email deve avere un formato valido"
        )
        String email
) {
}
