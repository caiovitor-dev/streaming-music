package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.CreateArtistDTO;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequestMapping("/artists")
@RequiredArgsConstructor
@RestController
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createArtist(
            @Valid CreateArtistDTO createArtist,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        UUID artistId = artistService.createArtist(createArtist,userDetails);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(artistId)
                .toUri();

        return ResponseEntity.created(location).build();

    }
}
