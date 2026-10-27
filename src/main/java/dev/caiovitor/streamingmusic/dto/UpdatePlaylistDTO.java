package dev.caiovitor.streamingmusic.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdatePlaylistDTO(

        String name,
        String description,
        Boolean isPublic,
        MultipartFile imagePlaylist
) {
}
