package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record SongRequestDTO(

        @NotBlank(message = "Title cannot be blank.")
        @Size(message = "Title can have a maximum of 100 characters.",max = 100)
        String title,

        @NotBlank(message = "Name artist cannot be blank.")
        @Size(message = "Artist name can have a maximum of 80 characters.",max = 80)
        String artist,

        @NotNull(message = "Image song cannot be blank.")
        MultipartFile imageSong,

        @NotBlank(message = "Title cannot be blank.")
        String lyrics,

        @NotNull(message = "Audio song cannot be blank.")
        MultipartFile audioSong

) {
}
