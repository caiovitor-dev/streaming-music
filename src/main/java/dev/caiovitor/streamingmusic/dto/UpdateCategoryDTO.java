package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdateCategoryDTO(


        @Size(max = 100, message = "can have a maximum of 100 characters.")
        @Pattern(regexp = "\\S.*", message = "must not be blank")
        String name,

        MultipartFile image,

        Boolean active
) {
}
