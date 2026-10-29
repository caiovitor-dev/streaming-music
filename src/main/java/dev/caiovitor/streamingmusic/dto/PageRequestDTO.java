package dev.caiovitor.streamingmusic.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PageRequestDTO (

        @Min(value = 0,message ="must be greater than or equal to 0")
        Integer page,

        @Min(value = 1,message = "must be greater than or equal to 1")
        @Max(value = 100,message = "must be less than or equal to 100")
        Integer size
){

    public PageRequestDTO{
        if (page == null) page = 0;
        if(size == null) size = 10;
    }
}
