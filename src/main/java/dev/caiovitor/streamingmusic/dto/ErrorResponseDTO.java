package dev.caiovitor.streamingmusic.dto;


import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int value,
        String message,
        String error,
        String path

) {
}
