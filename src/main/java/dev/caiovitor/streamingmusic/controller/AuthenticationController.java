package dev.caiovitor.streamingmusic.controller;


import dev.caiovitor.streamingmusic.dto.*;
import dev.caiovitor.streamingmusic.entity.User;
import dev.caiovitor.streamingmusic.service.AuthenticationService;
import dev.caiovitor.streamingmusic.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserCreateDTO userRegister){

        User user = authenticationService.registerUser(userRegister);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest){

        LoginResponseDTO response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO tokenRequest){

        RefreshTokenResult refreshTokenResult = refreshTokenService.rotateRefreshToken(tokenRequest.refreshToken());

        return ResponseEntity.ok(new TokenResponseDTO(
                refreshTokenResult.accessToken(),
                refreshTokenResult.refreshToken()));

    }
}
