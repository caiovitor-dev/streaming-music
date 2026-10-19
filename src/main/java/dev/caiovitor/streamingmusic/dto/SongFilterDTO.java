package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.Size;

public record SongFilterDTO(

        @Size(message = "Artist name can have a maximum of 80 characters.",max = 80)
        String artist,
        @Size(message = "Title can have a maximum of 100 characters.",max = 100)
        String title
) {
}
