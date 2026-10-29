package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;

public record PlaylistResponseDTO(
        String name,
        String description,
        String imageUrl,
        Boolean isPublic,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public PlaylistResponseDTO withImageUrl(String imageUrl) {
        return new PlaylistResponseDTO(
                name,
                description,
                imageUrl,
                isPublic,
                createdAt,
                updatedAt
        );
    }
}
