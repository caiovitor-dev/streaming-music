package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.*;
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
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/song")
@RestController

public class SongController {


    private final SongService songService;


    @PreAuthorize("hasRole('ARTIST')")
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
    public ResponseEntity<PageResponseDTO<SongResponseDTO>> getAllSongs(
            @PageableDefault(page = 0,size = 10,sort = "title",direction = Sort.Direction.DESC) Pageable pageable){

        PageResponseDTO<SongResponseDTO> allSongs = songService.findAllSongs(pageable);
        return ResponseEntity.ok(allSongs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDTO> findById(@PathVariable UUID id){

        SongResponseDTO songResponse = songService.findById(id);
        return ResponseEntity.ok(songResponse);

    }



    @GetMapping("/search")
    public ResponseEntity<PageResponseDTO<SongResponseDTO>> searchSongs(
            @Valid SongFilterDTO songFilter, @PageableDefault(size = 10, sort = "title",direction = Sort.Direction.ASC) Pageable pageable){

        PageResponseDTO<SongResponseDTO> songResponse = songService.searchSongs(songFilter, pageable);
        return ResponseEntity.ok(songResponse);


    }

    @PreAuthorize("hasRole('ARTIST')")
    @PatchMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateSong(@PathVariable UUID id, @Valid SongUpdateDTO songUpdate){

        songService.updateSong(songUpdate,id);
        return ResponseEntity.noContent().build();
    }
}
