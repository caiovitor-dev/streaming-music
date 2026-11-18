package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponseDTO(
        LocalDateTime timestamp,
        int value,
        String message,
        String error,
        String path,
        Map<String,String> errors
) {
}
