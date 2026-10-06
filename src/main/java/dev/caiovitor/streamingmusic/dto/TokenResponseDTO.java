package dev.caiovitor.streamingmusic.dto;

public record TokenResponseDTO(
        String accessToken,
        String refreshToken
) {
}
