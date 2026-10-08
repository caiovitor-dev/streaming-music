package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;

public record UserProfileResponseDTO(
        String name,
        String email,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
