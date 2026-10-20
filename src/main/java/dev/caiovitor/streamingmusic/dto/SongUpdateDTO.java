package dev.caiovitor.streamingmusic.dto;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record SongUpdateDTO(

        @Size(message = "Title can have a maximum of 100 characters.",max = 100)
        @Pattern(regexp = "\\S.*", message = "title must not be blank")
        String title,

        @Size(message = "Artist name can have a maximum of 80 characters.",max = 80)
        @Pattern(regexp = "\\S.*", message = "artist must not be blank")
        String artist,

        MultipartFile imageSong,
        MultipartFile audioSong

) {
}
