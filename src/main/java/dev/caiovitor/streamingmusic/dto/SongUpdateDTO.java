package dev.caiovitor.streamingmusic.dto;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record SongUpdateDTO(

        @Size(message = "Title can have a maximum of 100 characters.",max = 100)
        @Pattern(regexp = "\\S.*", message = "title must not be blank")
        String title,

        MultipartFile imageSong,
        MultipartFile audioSong

) {
}
