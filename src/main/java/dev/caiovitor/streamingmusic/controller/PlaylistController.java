package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.CreatePlaylistDTO;
import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/playlist")
@RestController
@RequiredArgsConstructor
public class PlaylistController {


    private final PlaylistService playlistService;

    @PreAuthorize("hasAnyRole('USER','ARTIST')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> savePlaylist(@Valid CreatePlaylistDTO createPlaylist, @AuthenticationPrincipal CustomUserDetails userDetails){

        Playlist playlist = playlistService.savePlaylist(createPlaylist, userDetails);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(playlist.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }
}
