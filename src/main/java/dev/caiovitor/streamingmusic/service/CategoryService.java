package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.*;
import dev.caiovitor.streamingmusic.entity.Category;
import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.exception.*;
import dev.caiovitor.streamingmusic.mapper.CategoryMapper;
import dev.caiovitor.streamingmusic.mapper.PlaylistMapper;
import dev.caiovitor.streamingmusic.repository.CategoryRepository;
import dev.caiovitor.streamingmusic.repository.PlaylistRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PlaylistService playlistService;
    private final PlaylistRepository playlistRepository;
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

    @Transactional
    public void removePlaylist(UUID categoryId, UUID playlistId) {

        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        category.getPlaylists().removeIf(playlist -> playlist.getId().equals(playlistId));
    }

    public List<CategoryResponseDTO> getAllCategories() {

      List<CategoryResponseDTO> categoryResponse= categoryRepository.findAllByActiveTrueOrderByPositionAscNameAsc().stream()
              .map(this::toResponse)
              .toList();

      return categoryResponse;
    }

    public CategoryResponseDTO findById(UUID categoryId) {

       return categoryRepository.findByIdAndActiveTrue(categoryId)
                .map(this::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

    }

    private CategoryResponseDTO toResponse(Category category){
        return categoryMapper.toDTO(category)
                .withImageUrl(s3Service.presignedUrl(category.getImageKey()).toString());
    }

    public PageResponseDTO<PlaylistResponseDTO> getCategoryPlaylist(UUID categoryId, PageRequestDTO pageRequest) {

        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(),
                Sort.by("name").and(Sort.by("id")));

        Page<PlaylistResponseDTO> playlistResponseDTO = playlistRepository.findPublicByCategoryId(categoryId,pageable)
                .map(playlistService::toResponse);

        return PageResponseDTO.from(playlistResponseDTO);
    }

    @Transactional
    public void updatePlaylist(UUID categoryId,  UpdateCategoryDTO updateCategory) {

        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if(categoryRepository.existsByNameIgnoreCaseAndIdNot(updateCategory.name(),category.getId())){
            throw new CategoryAlreadyExistsException("Category already exists");
        }

        String oldKey = null;


        if(updateCategory.image() != null){
            validateImage(updateCategory.image());
            oldKey = category.getImageKey();
            category.setImageKey(s3Service.uploadFile(updateCategory.image(), "images"));
        }

        categoryMapper.updateCategory(updateCategory,category);
        categoryRepository.save(category);

        if(oldKey != null){
            s3Service.deleteFile(oldKey);
        }
    }
}
