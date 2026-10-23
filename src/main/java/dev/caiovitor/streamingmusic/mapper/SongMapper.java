package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.SongRequestDTO;
import dev.caiovitor.streamingmusic.dto.SongResponseDTO;
import dev.caiovitor.streamingmusic.dto.SongUpdateDTO;
import dev.caiovitor.streamingmusic.entity.Song;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SongMapper {



    @Mapping(target = "imageUrl" ,ignore = true)
    @Mapping(target = "songUrl" ,ignore = true)
    public Song toEntity(SongRequestDTO songRequest);

    public SongResponseDTO toDTO(Song song);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imageUrl" ,ignore = true)
    @Mapping(target = "songUrl" ,ignore = true)
    void updateSong(SongUpdateDTO songUpdate, @MappingTarget Song song);
}
