package dev.caiovitor.streamingmusic.service;


import dev.caiovitor.streamingmusic.dto.SongRequestDTO;
import dev.caiovitor.streamingmusic.entity.Song;
import dev.caiovitor.streamingmusic.exception.FileSizeExceededException;
import dev.caiovitor.streamingmusic.mapper.SongMapper;
import dev.caiovitor.streamingmusic.repository.SongRepository;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;

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


}
