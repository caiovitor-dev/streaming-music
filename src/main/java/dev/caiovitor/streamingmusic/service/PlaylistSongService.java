package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.CreatePlaylistSongDTO;
import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.entity.PlaylistSong;
import dev.caiovitor.streamingmusic.entity.Song;
import dev.caiovitor.streamingmusic.exception.PlaylistOwnershipException;
import dev.caiovitor.streamingmusic.exception.SongAlreadyInPlaylistException;
import dev.caiovitor.streamingmusic.exception.SongNotFoundException;
import dev.caiovitor.streamingmusic.mapper.PlaylistSongMapper;
import dev.caiovitor.streamingmusic.repository.PlaylistSongRepository;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaylistSongService {

    private final SongService songService;
    private final PlaylistService playlistService;
    private final PlaylistSongMapper playlistSongMapper;
    private final PlaylistSongRepository playlistSongRepository;

    @Transactional
    public PlaylistSong createPlaylistSong(UUID playlistId,CreatePlaylistSongDTO createPlaylist, CustomUserDetails userDetails){

        
        Playlist playlist = playlistService.findById(playlistId);
        Song song = songService.findById(createPlaylist.songId());

        if(!playlist.getUser().getId().equals(userDetails.getUser().getId())){
            throw new PlaylistOwnershipException("You do not have permission to modify this playlist.");
        }
        if(playlistSongRepository.existsByPlaylistIdAndSongId(playlist.getId(),song.getId())){
            throw new SongAlreadyInPlaylistException("This song has already been added to this playlist");
        }

        int nextPosition = (int) playlistSongRepository.countByPlaylistId(playlist.getId()) + 1;


        int position = (createPlaylist.position() == null || createPlaylist.position()>nextPosition)
                ? nextPosition
                : createPlaylist.position();


        if(position < nextPosition){
            playlistSongRepository.changePosition(playlistId,position);
        }

        PlaylistSong playlistSong = playlistSongMapper.toEntity(createPlaylist);

        playlistSong.setPlaylist(playlist);
        playlistSong.setSong(song);
        playlistSong.setPosition(position);


        return playlistSongRepository.save(playlistSong);
    }

    @Transactional
    public void deletePlaylistSong(UUID playlistId, UUID songId, CustomUserDetails userDetails) {

        Playlist playlist = playlistService.findById(playlistId);

        if(!playlist.getUser().getId().equals(userDetails.getUser().getId())){
            throw new PlaylistOwnershipException("You do not have permission to modify this playlist.");
        }

        PlaylistSong playlistSong = playlistSongRepository.findByPlaylistIdAndSongId(playlistId,songId)
                .orElseThrow(() -> new SongNotFoundException("The song is not in the playlist."));


        int position = playlistSong.getPosition();;

        playlistSongRepository.delete(playlistSong);
        playlistSongRepository.flush();
        playlistSongRepository.shiftPositionsDown(playlistId,position);

    }
}
