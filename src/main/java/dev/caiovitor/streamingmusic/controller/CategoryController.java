package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.*;
import dev.caiovitor.streamingmusic.entity.Category;
import dev.caiovitor.streamingmusic.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping("/categories")
@RequiredArgsConstructor
@RestController
public class CategoryController {


    private final CategoryService categoryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createCategory(@Valid CreateCategoryDTO createCategory){

        Category category = categoryService.createCategory(createCategory);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(category.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}/playlists/{playlistId}")
    public ResponseEntity<Void> addPlaylist(@PathVariable UUID categoryId, @PathVariable UUID playlistId){

        categoryService.addPlaylist(categoryId,playlistId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}/playlists/{playlistId}")
    public ResponseEntity<Void> removePlaylist(@PathVariable UUID categoryId, @PathVariable UUID playlistId){

        categoryService.removePlaylist(categoryId,playlistId);
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){

        List<CategoryResponseDTO> allCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(allCategories);

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable UUID categoryId){

        CategoryResponseDTO categoryResponse = categoryService.findById(categoryId);
        return ResponseEntity.ok(categoryResponse);

    }

    @GetMapping("/{categoryId}/playlists")
    public ResponseEntity<PageResponseDTO<PlaylistResponseDTO>> getCategoryPlaylist(
            @PathVariable UUID categoryId,
            @Valid PageRequestDTO pageRequest
    ){

        PageResponseDTO<PlaylistResponseDTO> playlistResponse =
                categoryService.getCategoryPlaylist(categoryId,pageRequest);

        return ResponseEntity.ok(playlistResponse);
    }

    @PatchMapping(value = "/{categoryId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity <Void> updateCategory(@PathVariable UUID categoryId,@Valid UpdateCategoryDTO updateCategory){

        categoryService.updatePlaylist(categoryId,updateCategory);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{categoryId}/position")
    public ResponseEntity <Void> updatePosition(@PathVariable UUID categoryId,@Valid @RequestBody UpdateCategoryPositionDTO updateCategoryPosition){

        categoryService.updatePosition(categoryId,updateCategoryPosition.position());
        return ResponseEntity.noContent().build();
    }
}
