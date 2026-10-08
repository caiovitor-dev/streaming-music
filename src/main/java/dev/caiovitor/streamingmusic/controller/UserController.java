package dev.caiovitor.streamingmusic.controller;


import dev.caiovitor.streamingmusic.dto.UserProfileResponseDTO;
import dev.caiovitor.streamingmusic.dto.UserProfileUpdateDTO;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails){

        UserProfileResponseDTO userProfile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(userProfile);
    }

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserProfileUpdateDTO userUpdate){

        userService.updateUserProfile(userDetails.getUsername(),userUpdate);
        return ResponseEntity.noContent().build();
    }

}
