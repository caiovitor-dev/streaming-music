package dev.caiovitor.streamingmusic.dto;

public record LoginResponseDTO (
        String accessToken,
        String refreshToken
){
}
