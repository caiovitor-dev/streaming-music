package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.CreatePlaylistDTO;
import dev.caiovitor.streamingmusic.dto.UpdatePlaylistDTO;
import dev.caiovitor.streamingmusic.entity.Playlist;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {


    @Mapping(target = "imageUrl",ignore = true)
    Playlist toEntity(CreatePlaylistDTO createPlayList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imageUrl",ignore = true)
    void updatePlaylist(UpdatePlaylistDTO updatePlaylist,@MappingTarget Playlist playlist);
}
