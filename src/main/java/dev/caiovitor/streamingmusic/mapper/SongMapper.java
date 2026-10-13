package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.SongRequestDTO;
import dev.caiovitor.streamingmusic.dto.SongResponseDTO;
import dev.caiovitor.streamingmusic.entity.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {


    public Song toEntity(SongRequestDTO songRequest);

    public SongResponseDTO toDTO(Song song);
}
