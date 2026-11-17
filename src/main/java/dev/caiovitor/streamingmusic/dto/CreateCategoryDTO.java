package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public record CreateCategoryDTO(

        @NotBlank(message = "it cannot be blank")
        @Size(max = 100, message = "can have a maximum of 100 characters.")
        String name,

        @NotNull(message = "it cannot be null")
        MultipartFile image,

        Boolean active

) {

    public CreateCategoryDTO{
        if(active()==null)
            active = true;
    }
}

