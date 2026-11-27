package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.ArtistResponseDTO;
import dev.caiovitor.streamingmusic.dto.CreateArtistDTO;
import dev.caiovitor.streamingmusic.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistMapper {


    @Mapping(target = "profileImageKey",ignore = true)
    @Mapping(target = "bannerImageKey",ignore = true)
    Artist toEntity(CreateArtistDTO createArtist);

    @Mapping(target = "profileImageUrl",source = "profileImageKey")
    ArtistResponseDTO toDTO(Artist artist);
}
