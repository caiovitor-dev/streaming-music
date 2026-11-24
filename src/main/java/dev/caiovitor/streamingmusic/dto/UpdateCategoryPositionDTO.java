package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCategoryPositionDTO (

        @NotNull(message = "it cannot be blank")
        @Positive(message = "must be greater than or equal to 1")
        Integer position
){
}
