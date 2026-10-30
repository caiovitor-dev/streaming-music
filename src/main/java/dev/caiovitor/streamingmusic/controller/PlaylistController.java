package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.*;
import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.PlaylistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequestMapping("/playlist")
@RestController
@RequiredArgsConstructor
public class PlaylistController {


    private final PlaylistService playlistService;

    @PreAuthorize("hasAnyRole('USER','ARTIST')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPlaylist(@Valid CreatePlaylistDTO createPlaylist, @AuthenticationPrincipal CustomUserDetails userDetails){

        Playlist playlist = playlistService.createPlaylist(createPlaylist, userDetails);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(playlist.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @PreAuthorize("hasAnyRole('USER','ARTIST')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePlaylist(@PathVariable UUID id,
                                               UpdatePlaylistDTO updatePlaylist,
                                               @AuthenticationPrincipal CustomUserDetails userDetails
    ){

        playlistService.updatePlaylist(updatePlaylist,id,userDetails);
        return ResponseEntity.noContent().build();

    }

    @PreAuthorize("hasAnyRole('USER','ARTIST')")
    @GetMapping("/me")
    public ResponseEntity<PageResponseDTO<PlaylistResponseDTO>> findMyPlaylist(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid PageRequestDTO pageRequest
    ){

        PageResponseDTO<PlaylistResponseDTO> playlistResponse = playlistService.findByOwner(userDetails,pageRequest);
        return ResponseEntity.ok(playlistResponse);
    }

    @PreAuthorize("hasAnyRole('USER','ARTIST')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        playlistService.deletePlaylist(id,userDetails);
        return ResponseEntity.noContent().build();
    }
}
