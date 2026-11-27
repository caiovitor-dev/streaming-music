package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SongResponseDTO(
        UUID id,
        String title,
        String songUrl,
        String imageUrl,
        ArtistResponseDTO artist,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public SongResponseDTO withImageAndAudioUrl(String songUrl,String imageUrl,ArtistResponseDTO artist){
        return new SongResponseDTO(
                id,
                title,
                songUrl,
                imageUrl,
                artist,
                createdAt,
                updatedAt
        );
    }
}
