package dev.caiovitor.streamingmusic.dto;

public record TokenResultDTO(
        String accessToken,
        String refreshToken
) {
}
