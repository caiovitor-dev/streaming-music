package dev.caiovitor.streamingmusic.controller;


import dev.caiovitor.streamingmusic.dto.*;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<PageResponseDTO<UserProfileResponseDTO>> getAllUsers(@Valid PageRequestDTO pageRequest){


        PageResponseDTO<UserProfileResponseDTO> allUsers = userService.findAllUsers(pageRequest);
        return ResponseEntity.ok(allUsers);


    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/role")
    public ResponseEntity<Void> updateRole(@PathVariable UUID id, @RequestBody UserUpdateRoleDTO roleUpdate){

        userService.updateUserRole(id,roleUpdate.roles());
        return ResponseEntity.noContent().build();
    }




}
