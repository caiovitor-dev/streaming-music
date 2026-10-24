package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record CreatePlaylistDTO(

        @NotBlank(message = "Name cannot be blank.")
        @Size(message = "Name can have a maximum of 80 characters.",max = 80)
        String name,

        String description,

        @NotNull(message = "isPublic must be provided.")
        Boolean isPublic,

        MultipartFile imagePlaylist
) {
}
