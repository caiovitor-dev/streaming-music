package dev.caiovitor.streamingmusic.dto;

import java.time.LocalDateTime;

public record PlaylistSongResponseDTO(

        int position,
        LocalDateTime createdAt,
        SongResponseDTO song
){
}
