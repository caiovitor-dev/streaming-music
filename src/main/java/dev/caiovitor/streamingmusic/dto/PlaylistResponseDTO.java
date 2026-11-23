package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlaylistResponseDTO(
        UUID id,
        String name,
        String description,
        String imageUrl,
        Boolean isPublic,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public PlaylistResponseDTO withImageUrl(String imageUrl) {
        return new PlaylistResponseDTO(
                id,
                name,
                description,
                imageUrl,
                isPublic,
                createdAt,
                updatedAt
        );
    }
}
