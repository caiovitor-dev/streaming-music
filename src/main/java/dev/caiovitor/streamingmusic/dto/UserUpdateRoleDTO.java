package dev.caiovitor.streamingmusic.dto;



import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserUpdateRoleDTO(

        @NotBlank(message = "Roles cannot be blank.")
        Set<String> roles
) {
}
