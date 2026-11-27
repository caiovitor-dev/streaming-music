package dev.caiovitor.streamingmusic.dto;

import java.util.UUID;

public record ArtistResponseDTO(
        UUID id,
        String name,
        String profileImageUrl
) {

    public  ArtistResponseDTO withProfileImageUrl(String profileImageUrl){
        return new ArtistResponseDTO(
                id,
                name,
                profileImageUrl
        );
    }
}
