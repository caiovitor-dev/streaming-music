package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.CategoryResponseDTO;
import dev.caiovitor.streamingmusic.dto.CreateCategoryDTO;
import dev.caiovitor.streamingmusic.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "imageKey",ignore = true)
    Category toEntity(CreateCategoryDTO createCategory);

    @Mapping(target = "imageUrl",ignore = true)
    CategoryResponseDTO toDTO(Category category);

}
