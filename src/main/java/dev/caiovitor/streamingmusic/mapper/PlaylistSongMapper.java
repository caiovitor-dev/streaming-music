package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.CreatePlaylistSongDTO;
import dev.caiovitor.streamingmusic.dto.PlaylistSongResponseDTO;
import dev.caiovitor.streamingmusic.entity.PlaylistSong;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistSongMapper {

    @Mapping(target = "song",ignore = true)
    @Mapping(target = "playlist",ignore = true)
    PlaylistSong toEntity(CreatePlaylistSongDTO cretePlaylist);

    PlaylistSongResponseDTO toDTO(PlaylistSong playlistSong);
}
