package dev.caiovitor.streamingmusic.mapper;

import dev.caiovitor.streamingmusic.dto.CategoryResponseDTO;
import dev.caiovitor.streamingmusic.dto.CreateCategoryDTO;
import dev.caiovitor.streamingmusic.dto.UpdateCategoryDTO;
import dev.caiovitor.streamingmusic.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "imageKey",ignore = true)
    Category toEntity(CreateCategoryDTO createCategory);

    @Mapping(target = "imageUrl",ignore = true)
    CategoryResponseDTO toDTO(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imageKey" ,ignore = true)
    void updateCategory(UpdateCategoryDTO updateCategory,@MappingTarget Category category);

}
