package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDTO (
        UUID id,
        String name,
        String imageUrl,
        boolean active,
        int position,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){

    public CategoryResponseDTO withImageUrl(String imageUrl){
        return new CategoryResponseDTO(
                id,
                name,
                imageUrl,
                active,
                position,
                createdAt,
                updatedAt
        );
    }
}
