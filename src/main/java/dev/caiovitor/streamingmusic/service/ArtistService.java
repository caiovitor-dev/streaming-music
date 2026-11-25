package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.CreateArtistDTO;
import dev.caiovitor.streamingmusic.entity.Artist;
import dev.caiovitor.streamingmusic.exception.*;
import dev.caiovitor.streamingmusic.mapper.ArtistMapper;
import dev.caiovitor.streamingmusic.repository.ArtistRepository;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final S3Service s3Service;



    public UUID createArtist(CreateArtistDTO createArtist, CustomUserDetails userDetails) {
        if(artistRepository.existsByUserId(userDetails.getUser().getId())){
            throw new ArtistAlreadyExistsException("Artist already exists for this user");
        }

        Artist artist = artistMapper.toEntity(createArtist);
        artist.setUser(userDetails.getUser());

        if (createArtist.profileImage() != null) {
            validateImage(createArtist.profileImage());
            artist.setProfileImageKey(s3Service.uploadFile(createArtist.profileImage(),"images"));
        }

        if (createArtist.bannerImage() != null) {
            validateImage(createArtist.bannerImage());
           artist.setBannerImageKey(s3Service.uploadFile(createArtist.bannerImage(), "images"));
        }

        artistRepository.save(artist);
        return artist.getId();
    }

    private void validateImage(MultipartFile image){

        long maxImageByte = DataSize.ofMegabytes(5).toBytes();

        if(!"image/jpeg".equals(image.getContentType()) && !"image/png".equals(image.getContentType())){
            throw new InvalidFileException("Only JPEG and PNG images are allowed.");
        }

        if(image.isEmpty()){
            throw  new FileEmptyException("The file is empty");
        }

        if (image.getSize() > maxImageByte){
            throw  new FileSizeExceededException("The image exceeded the 5 MB size limit.");
        }

    }
}
