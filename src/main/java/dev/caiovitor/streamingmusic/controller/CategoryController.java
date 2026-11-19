package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.CategoryResponseDTO;
import dev.caiovitor.streamingmusic.dto.CreateCategoryDTO;
import dev.caiovitor.streamingmusic.entity.Category;
import dev.caiovitor.streamingmusic.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
