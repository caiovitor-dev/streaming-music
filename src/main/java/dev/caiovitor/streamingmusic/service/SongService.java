package dev.caiovitor.streamingmusic.service;


import dev.caiovitor.streamingmusic.Specification.SongSpecification;
import dev.caiovitor.streamingmusic.dto.*;
import dev.caiovitor.streamingmusic.entity.Song;
import dev.caiovitor.streamingmusic.exception.*;
import dev.caiovitor.streamingmusic.mapper.SongMapper;
import dev.caiovitor.streamingmusic.repository.SongRepository;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;


import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SongService {


    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final S3Service s3Service;

    public Song createSong (SongRequestDTO songRequest, CustomUserDetails userDetails) {

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

    public PageResponseDTO<SongResponseDTO> searchSongs(SongFilterDTO songFilter, PageRequestDTO pageRequest){

        Specification<Song> specification = Specification
                .where(SongSpecification.artistContains(songFilter.artist()))
                .and(SongSpecification.titleContains(songFilter.title()));

        Pageable pageable =PageRequest.of(pageRequest.page(),pageRequest.size(), Sort.by("title").ascending());

        Page<SongResponseDTO> songResponse = songRepository
                .findAll(specification,pageable)
                .map(songMapper::toDTO);

        return PageResponseDTO.from(songResponse);

    }

    @Transactional
    public void updateSong(SongUpdateDTO songUpdate,UUID id) {

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song not found."));

        if(songUpdate.imageSong() != null){
            validateImage(songUpdate.imageSong());
            song.setImageUrl(s3Service.uploadFile(songUpdate.imageSong(), "images"));
        }

        if(songUpdate.audioSong() != null ){
            validateAudio(songUpdate.audioSong());
            song.setSongUrl(s3Service.uploadFile(songUpdate.audioSong(), "audios"));
        }

        String oldImage = song.getImageUrl();
        String oldAudio = song.getSongUrl();

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

        long maxImageByte = DataSize.ofMegabytes(5).toBytes();

        if(!"image/jpeg".equals(imageSong.getContentType()) && !"image/png".equals(imageSong.getContentType())){
            throw new InvalidFileException("Only JPEG and PNG images are allowed.");
        }

        if(imageSong.isEmpty()){
            throw  new FileEmptyException("The file is empty");
        }

        if (imageSong.getSize() > maxImageByte){
            throw  new FileSizeExceededException("The image exceeded the 5 MB size limit.");
        }

    }

    private void validateAudio(MultipartFile audioSong){

        long maxSongByte = DataSize.ofMegabytes(30).toBytes();

        if("audio/flac".equals(audioSong.getContentType()) && ! "audio/mp3".equals(audioSong.getContentType())){
            throw new InvalidFileException("Only MP3 and FLAC audios are allowed.");
        }
        if(audioSong.isEmpty()){
            throw new FileEmptyException("The file is empty");
        }
        if (audioSong.getSize() > maxSongByte){
            throw new FileSizeExceededException("The song exceeded the 30 MB size limit.");
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
