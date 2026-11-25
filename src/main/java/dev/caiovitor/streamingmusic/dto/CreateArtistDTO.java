package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record CreateArtistDTO(

        @NotBlank(message = "it cannot be blank")
        @Size(max = 80, message = "can have a maximum of 80 characters.")
        String name,

        @Pattern(regexp = "\\S.*",flags = Pattern.Flag.DOTALL, message = "must not be blank")
        @Size(max = 600,message = "can have a maximum of 600 characters.")
        String bio,

        MultipartFile profileImage,

        MultipartFile bannerImage


) {
}
