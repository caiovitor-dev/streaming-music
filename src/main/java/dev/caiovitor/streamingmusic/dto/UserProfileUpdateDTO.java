package dev.caiovitor.streamingmusic.dto;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateDTO(

        @Size(message = "must be a maximum of 80 characters.",max = 80)
        @Pattern(regexp = "\\S.*", message = "must not be blank")
        String name,

        String imageUrl
){
}
