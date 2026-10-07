package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
