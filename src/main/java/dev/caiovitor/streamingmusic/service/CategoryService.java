package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.CreateCategoryDTO;
import dev.caiovitor.streamingmusic.entity.Category;
import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.exception.*;
import dev.caiovitor.streamingmusic.mapper.CategoryMapper;
import dev.caiovitor.streamingmusic.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PlaylistService playlistService;
    private final S3Service s3Service;

    @Transactional
    public Category createCategory(CreateCategoryDTO createCategory){

        if(categoryRepository.existsByNameIgnoreCase(createCategory.name())){
            throw new CategoryAlreadyExistsException("Category already exists");
        }

        validateImage(createCategory.image());
        Category category = categoryMapper.toEntity(createCategory);


        category.setPosition(categoryRepository.findMaxPosition()+1);
        category.setImageKey(s3Service.uploadFile(createCategory.image(),"images"));
        categoryRepository.save(category);

        return category;
    }

    private void validateImage(MultipartFile imageCategory){

        long maxImageByte = DataSize.ofMegabytes(5).toBytes();

        if(imageCategory.isEmpty()){
            throw new FileEmptyException("This image is empty");
        }
        if(imageCategory.getSize() > maxImageByte){
            throw  new FileSizeExceededException("The image exceeded the 5 MB size limit.");
        }
        if(!"image/jpeg".equals(imageCategory.getContentType()) && !"image/png".equals(imageCategory.getContentType())){
            throw new InvalidFileException("Only JPEG and PNG images are allowed.");
        }

    }

    @Transactional
    public void addPlaylist(UUID categoryId, UUID playlistId) {
        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Playlist playlist =playlistService.findById(playlistId);

        if(!playlist.isPublic()){
            throw new PlaylistNotPublicException("Playlist not public");
        }

        category.getPlaylists().add(playlist);
    }
}
