package dev.caiovitor.streamingmusic.service;


import dev.caiovitor.streamingmusic.Specification.SongSpecification;
import dev.caiovitor.streamingmusic.dto.PageResponseDTO;
import dev.caiovitor.streamingmusic.dto.SongFilterDTO;
import dev.caiovitor.streamingmusic.dto.SongRequestDTO;
import dev.caiovitor.streamingmusic.dto.SongResponseDTO;
import dev.caiovitor.streamingmusic.entity.Song;
import dev.caiovitor.streamingmusic.exception.FileSizeExceededException;
import dev.caiovitor.streamingmusic.mapper.SongMapper;
import dev.caiovitor.streamingmusic.repository.SongRepository;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SongService {


    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final S3Service s3Service;

    public Song saveSong (SongRequestDTO songRequest, CustomUserDetails userDetails){

        long maxImageByte = DataSize.ofMegabytes(5).toBytes();
        long maxSongByte = DataSize.ofMegabytes(10).toBytes();

        if(songRequest.imageSong().getSize() > maxImageByte ){
           throw  new FileSizeExceededException("The image exceeded the 5 MB size limit.");
        }

        if(songRequest.audioSong().getSize() > maxSongByte ){
           throw  new FileSizeExceededException("The song exceeded the 30 MB size limit.");
        }

        Song song = songMapper.toEntity(songRequest);

        String image = s3Service.uploadFile(songRequest.imageSong(), "images");
        String audio = s3Service.uploadFile(songRequest.audioSong(), "audios");

        song.setSongUrl(audio);
        song.setImageUrl(image);
        song.setUser(userDetails.getUser());

        return songRepository.save(song);
    }


    public PageResponseDTO<SongResponseDTO> findAllSongs(Pageable pageable) {

        Page<SongResponseDTO> content = songRepository.findAll(pageable).map(songMapper::toDTO);
        return PageResponseDTO.from(content);

    }

    public SongResponseDTO findById(UUID id){

       Song song = songRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Song not found."));

        return songMapper.toDTO(song);

    }

    public PageResponseDTO<SongResponseDTO> searchSongs(SongFilterDTO songFilter,Pageable pageable){

        Specification<Song> specification = Specification
                .where(SongSpecification.artistContains(songFilter.artist()))
                .and(SongSpecification.titleContains(songFilter.title()));


        Page<SongResponseDTO> songResponse = songRepository
                .findAll(specification,pageable)
                .map(songMapper::toDTO);

        return PageResponseDTO.from(songResponse);

    }
}
