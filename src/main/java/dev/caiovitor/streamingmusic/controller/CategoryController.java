package dev.caiovitor.streamingmusic.controller;

import dev.caiovitor.streamingmusic.dto.CreateCategoryDTO;
import dev.caiovitor.streamingmusic.entity.Category;
import dev.caiovitor.streamingmusic.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

}
