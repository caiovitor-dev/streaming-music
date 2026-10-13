package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.SongRequestDTO;
import dev.caiovitor.streamingmusic.entity.Song;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/song")
@RestController
@PreAuthorize("hasRole('ARTIST')")
public class SongController {


    private final SongService songService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveSong(@Valid @ModelAttribute SongRequestDTO songRequest, @AuthenticationPrincipal CustomUserDetails userDetails){

        Song song = songService.saveSong(songRequest,userDetails);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(song.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
