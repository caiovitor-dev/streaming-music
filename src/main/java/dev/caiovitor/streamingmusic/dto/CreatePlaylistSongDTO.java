package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreatePlaylistSongDTO(


        @NotNull(message = "it cannot be null")
        UUID songId,

        @Positive(message = "only positive numbers  ")
        Integer position
) {
}
