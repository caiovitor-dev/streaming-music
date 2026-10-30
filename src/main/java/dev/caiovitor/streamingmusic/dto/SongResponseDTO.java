package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;

public record SongResponseDTO(
        String title,
        String artist,
        String songUrl,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public SongResponseDTO withImageAndAudioUrl(String songUrl,String imageUrl){
        return new SongResponseDTO(
                title,
                artist,
                songUrl,
                imageUrl,
                createdAt,
                updatedAt
        );
    }
}
