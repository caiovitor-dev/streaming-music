package dev.caiovitor.streamingmusic.service;


import dev.caiovitor.streamingmusic.Specification.SongSpecification;
import dev.caiovitor.streamingmusic.dto.*;
import dev.caiovitor.streamingmusic.entity.Song;
import dev.caiovitor.streamingmusic.exception.*;
import dev.caiovitor.streamingmusic.mapper.SongMapper;
import dev.caiovitor.streamingmusic.repository.SongRepository;
import dev.caiovitor.streamingmusic.security.CustomAccessDeniedHandler;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SongService {


    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final S3Service s3Service;

    public Song saveSong (SongRequestDTO songRequest, CustomUserDetails userDetails) {

        long maxImageByte = DataSize.ofMegabytes(5).toBytes();
        long maxSongByte = DataSize.ofMegabytes(30).toBytes();

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

    @Transactional
    public void updateSong(SongUpdateDTO songUpdate,UUID id) {

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found."));

        validateAudio(songUpdate.audioSong());
        validateImage(songUpdate.imageSong());

        String oldImage = song.getImageUrl();
        String oldAudio = song.getSongUrl();


        if(songUpdate.imageSong() != null){
        song.setImageUrl(s3Service.uploadFile(songUpdate.imageSong(), "images"));
        }

        if(songUpdate.audioSong() != null){
        song.setSongUrl(s3Service.uploadFile(songUpdate.audioSong(), "audios"));
        }

        songMapper.updateSong(songUpdate,song);
        songRepository.save(song);

        if(songUpdate.imageSong() != null){
            s3Service.deleteFile(oldImage);
        }

        if(songUpdate.audioSong() != null) {
            s3Service.deleteFile(oldAudio);
        }
    }

    private void validateImage(MultipartFile imageSong){

        if(imageSong == null){
            return;
        }
        long maxImageByte = DataSize.ofMegabytes(5).toBytes();

        if (imageSong.getSize() > maxImageByte){
            throw  new FileSizeExceededException("The image exceeded the 5 MB size limit.");
        }

        if(imageSong.isEmpty()){
            throw  new InvalidFileException("The file is empty");
        }
    }

    private void validateAudio(MultipartFile audioSong){

        if(audioSong == null){
            return;
        }

        long maxSongByte = DataSize.ofMegabytes(30).toBytes();

        if (audioSong.getSize() > maxSongByte){
            throw new FileSizeExceededException("The song exceeded the 30 MB size limit.");
        }

        if(audioSong.isEmpty()){
            throw new InvalidFileException("The file is empty");
        }

    }

    @Transactional
    public void deleteSong(UUID id,CustomUserDetails userDetails) {

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song not found."));

        if(song.getId() != userDetails.getUser().getId()){
            throw new SongOwnershipException("You can't delete this song.");
        }

        String image = song.getImageUrl();
        String audio = song.getSongUrl();

        songRepository.delete(song);

        s3Service.deleteFile(image);
        s3Service.deleteFile(audio);


    }
}
