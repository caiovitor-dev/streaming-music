package dev.caiovitor.streamingmusic.controller;
import dev.caiovitor.streamingmusic.dto.CreatePlaylistSongDTO;
import dev.caiovitor.streamingmusic.entity.PlaylistSong;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.PlaylistSongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("playlists/{playlistId}/songs")
@RestController
@PreAuthorize("hasAnyRole('USER','ARTIST')")
public class PlaylistSongController {

    private final PlaylistSongService playlistSongService;

    @PostMapping
    public ResponseEntity<Void> createPlaylistSong(
            @PathVariable UUID playlistId,
            @Valid @RequestBody CreatePlaylistSongDTO createPlaylist,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        PlaylistSong playlistSong = playlistSongService.createPlaylistSong(playlistId,createPlaylist,userDetails);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(playlistSong.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> deletePlaylistSong(
            @PathVariable UUID playlistId,
            @PathVariable UUID songId,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        playlistSongService.deletePlaylistSong(playlistId,songId,userDetails);
        return ResponseEntity.noContent().build();

    }
}
