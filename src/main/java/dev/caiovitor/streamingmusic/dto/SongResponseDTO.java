package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SongResponseDTO(
        UUID id,
        String title,
        String artist,
        String songUrl,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public SongResponseDTO withImageAndAudioUrl(String songUrl,String imageUrl){
        return new SongResponseDTO(
                id,
                title,
                artist,
                songUrl,
                imageUrl,
                createdAt,
                updatedAt
        );
    }
}
