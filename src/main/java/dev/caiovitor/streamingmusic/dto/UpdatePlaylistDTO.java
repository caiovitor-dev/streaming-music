package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdatePlaylistDTO(

        @Size(message = "must be a maximum of 80 characters.",max = 80)
        @Pattern(regexp = "\\S.*", message = "must not be blank")
        String name,

        @Size(message = "must be a maximum of 300 characters.",max = 300)
        @Pattern(regexp = "\\S.*", message = "must not be blank")
        String description,

        Boolean isPublic,

        MultipartFile imagePlaylist
) {
}
