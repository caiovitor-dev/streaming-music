package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.CreatePlaylistDTO;
import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.exception.FileSizeExceededException;
import dev.caiovitor.streamingmusic.exception.InvalidFileException;
import dev.caiovitor.streamingmusic.mapper.PlaylistMapper;
import dev.caiovitor.streamingmusic.repository.PlaylistRepository;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final S3Service s3Service;
    

    public Playlist savePlaylist(CreatePlaylistDTO createPlaylist, CustomUserDetails userDetails)  {

        if(createPlaylist.imagePlaylist() != null){
            validateImage(createPlaylist.imagePlaylist());
        }

        Playlist playlist = playlistMapper.toEntity(createPlaylist);

        playlist.setUser(userDetails.getUser());
        playlistRepository.save(playlist);

        if(createPlaylist.imagePlaylist() != null){
            playlist.setImageUrl(s3Service.uploadFile(createPlaylist.imagePlaylist(), "images"));
        }

        return playlist;
    }

    private void validateImage(MultipartFile imagePlaylist){

        long maxImageByte = DataSize.ofMegabytes(5).toBytes();

        if(imagePlaylist.getSize() > maxImageByte ){
            throw  new FileSizeExceededException("The image exceeded the 5 MB size limit.");
        }
        if(!"image/jpeg".equals(imagePlaylist.getContentType()) && !"image/png".equals(imagePlaylist.getContentType())){
            throw new InvalidFileException("Only JPEG and PNG images are allowed.");
        }

    }
}
