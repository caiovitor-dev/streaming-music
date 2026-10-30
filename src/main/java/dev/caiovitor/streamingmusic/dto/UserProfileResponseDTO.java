package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;

public record UserProfileResponseDTO(
        String name,
        String email,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public UserProfileResponseDTO withImageUrl(String imageUrl){
        return new UserProfileResponseDTO(
                name,
                email,
                imageUrl,
                createdAt,
                updatedAt
        );
    }
}
