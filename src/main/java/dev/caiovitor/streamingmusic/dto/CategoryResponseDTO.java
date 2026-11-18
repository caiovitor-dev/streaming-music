package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;

public record CategoryResponseDTO (

        String name,
        String imageUrl,
        boolean active,
        int position,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){

    public CategoryResponseDTO withImageUrl(String imageUrl){
        return new CategoryResponseDTO(
                name,
                imageUrl,
                active,
                position,
                createdAt,
                updatedAt
        );
    }
}
