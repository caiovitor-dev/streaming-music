package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.CreatePlaylistDTO;
import dev.caiovitor.streamingmusic.entity.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {


    @Mapping(target = "imageUrl",ignore = true)
    Playlist toEntity(CreatePlaylistDTO createPlayList);

}
