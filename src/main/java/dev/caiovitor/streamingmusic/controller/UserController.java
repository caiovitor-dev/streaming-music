package dev.caiovitor.streamingmusic.controller;


import dev.caiovitor.streamingmusic.dto.PageResponseDTO;
import dev.caiovitor.streamingmusic.dto.UserProfileResponseDTO;
import dev.caiovitor.streamingmusic.dto.UserProfileUpdateDTO;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails){

        UserProfileResponseDTO userProfile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(userProfile);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserProfileUpdateDTO userUpdate){

        userService.updateUserProfile(userDetails.getUsername(),userUpdate);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponseDTO<UserProfileResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page ,
            @RequestParam(defaultValue = "10") int size){


        PageResponseDTO<UserProfileResponseDTO> allUsers = userService.findAllUsers(page, size);
        return ResponseEntity.ok(allUsers);


    }


}
