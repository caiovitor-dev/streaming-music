package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.PageResponseDTO;
import dev.caiovitor.streamingmusic.dto.SongRequestDTO;
import dev.caiovitor.streamingmusic.dto.SongResponseDTO;
import dev.caiovitor.streamingmusic.entity.Song;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.SongService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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

    @GetMapping
    ResponseEntity<PageResponseDTO<SongResponseDTO>> getAllSongs(
            @PageableDefault(page = 0,size = 10,sort = "title",direction = Sort.Direction.DESC) Pageable pageable){

        PageResponseDTO<SongResponseDTO> allSongs = songService.findAllSongs(pageable);
        return ResponseEntity.ok(allSongs);
    }
}
