package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.NotBlank;


public record RefreshTokenRequestDTO(
        @NotBlank(message = "Token cannot be empty")
        String refreshToken
) {
}
