package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.*;
import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.exception.*;
import dev.caiovitor.streamingmusic.mapper.PlaylistMapper;
import dev.caiovitor.streamingmusic.repository.PlaylistRepository;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final S3Service s3Service;


    public Playlist createPlaylist(CreatePlaylistDTO createPlaylist, CustomUserDetails userDetails)  {

        if(createPlaylist.imagePlaylist() != null){
            validateImage(createPlaylist.imagePlaylist());
        }

        Playlist playlist = playlistMapper.toEntity(createPlaylist);

        playlist.setUser(userDetails.getUser());

        if(createPlaylist.imagePlaylist() != null){
            playlist.setImageUrl(s3Service.uploadFile(createPlaylist.imagePlaylist(), "images"));
        }

        return playlistRepository.save(playlist);
    }

    @Transactional
    public void updatePlaylist(UpdatePlaylistDTO updatePlaylist,UUID id,CustomUserDetails userDetails){

        Playlist playlist =playlistRepository.findById(id)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found."));

        if(!playlist.getUser().getId() .equals(userDetails.getUser().getId())){
            throw new PlaylistOwnershipException("You do not have permission to modify this playlist.");
        }

        if(updatePlaylist.imagePlaylist() != null){
            validateImage(updatePlaylist.imagePlaylist());
            playlist.setImageUrl(s3Service.uploadFile(updatePlaylist.imagePlaylist(), "images"));
        }

        String oldImage = playlist.getImageUrl();

        playlistMapper.updatePlaylist(updatePlaylist,playlist);

        if(updatePlaylist.imagePlaylist() != null && oldImage != null){
            s3Service.deleteFile(oldImage);
        }
    }

    private void validateImage(MultipartFile imagePlaylist){

        long maxImageByte = DataSize.ofMegabytes(5).toBytes();

        if(imagePlaylist.isEmpty()){
            throw new FileEmptyException("The file is empty.");
        }
        if(imagePlaylist.getSize() > maxImageByte ){
            throw  new FileSizeExceededException("The image exceeded the 5 MB size limit.");
        }
        if(!"image/jpeg".equals(imagePlaylist.getContentType()) && !"image/png".equals(imagePlaylist.getContentType())){
            throw new InvalidFileException("Only JPEG and PNG images are allowed.");
        }

    }

    public PageResponseDTO<PlaylistResponseDTO> findByOwner(CustomUserDetails userDetails, PageRequestDTO pageRequest) {

        Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), Sort.by("name").ascending());

        Page<PlaylistResponseDTO> playlistResponse = playlistRepository.findAllByUserId(userDetails.getUser().getId(), pageable)
                .map(playlist -> {

                    PlaylistResponseDTO dto = playlistMapper.toDTO(playlist);
                    return dto.withImageUrl(resolveImageUrl(playlist.getImageUrl()));
                });

        return PageResponseDTO.from(playlistResponse);
    }

    @Transactional
    public void deletePlaylist(UUID id,CustomUserDetails userDetails) {

       Playlist playlist =  playlistRepository.findById(id)
               .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));

       if(!playlist.getUser().getId().equals(userDetails.getUser().getId())){
           throw new PlaylistOwnershipException("You do not have permission to delete this playlist.");
       }

       String image = playlist.getImageUrl();
       playlistRepository.delete(playlist);

       if(image !=null){
           s3Service.deleteFile(image);
       }

    }

    private String resolveImageUrl(String key) {
        return key == null ? null : s3Service.presignedUrl(key).toString();
    }

}
