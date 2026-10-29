package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.CreatePlaylistDTO;
import dev.caiovitor.streamingmusic.dto.PageResponseDTO;
import dev.caiovitor.streamingmusic.dto.PlaylistResponseDTO;
import dev.caiovitor.streamingmusic.dto.UpdatePlaylistDTO;
import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @PageableDefault(size = 10,sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ){

        PageResponseDTO<PlaylistResponseDTO> playlistResponse = playlistService.findByOwner(userDetails, pageable);
        return ResponseEntity.ok(playlistResponse);
    }
}
