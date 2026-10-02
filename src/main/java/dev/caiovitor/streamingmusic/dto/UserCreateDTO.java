package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(

        @NotBlank(message = "Name cannot be blank.")
        @Size(message = "Name can have a maximum of 80 characters.",max = 80)
        String name,

        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        String password
) {
}
