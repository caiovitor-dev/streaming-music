package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.*;
import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.entity.PlaylistSong;
import dev.caiovitor.streamingmusic.entity.Song;
import dev.caiovitor.streamingmusic.exception.PlaylistNotFoundException;
import dev.caiovitor.streamingmusic.exception.PlaylistOwnershipException;
import dev.caiovitor.streamingmusic.exception.SongAlreadyInPlaylistException;
import dev.caiovitor.streamingmusic.exception.SongNotFoundException;
import dev.caiovitor.streamingmusic.mapper.PlaylistSongMapper;
import dev.caiovitor.streamingmusic.mapper.SongMapper;
import dev.caiovitor.streamingmusic.repository.PlaylistSongRepository;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PlaylistSong createPlaylistSong(UUID playlistId, CreatePlaylistSongDTO createPlaylist, CustomUserDetails userDetails) {


        Playlist playlist = playlistService.findById(playlistId);
        Song song = songService.findById(createPlaylist.songId());

        if (!playlist.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new PlaylistOwnershipException("You do not have permission to modify this playlist.");
        }
        if (playlistSongRepository.existsByPlaylistIdAndSongId(playlist.getId(), song.getId())) {
            throw new SongAlreadyInPlaylistException("This song has already been added to this playlist");
        }

        int nextPosition = (int) playlistSongRepository.countByPlaylistId(playlist.getId()) + 1;


        int position = (createPlaylist.position() == null || createPlaylist.position() > nextPosition)
                ? nextPosition
                : createPlaylist.position();


        if (position < nextPosition) {
            playlistSongRepository.changePosition(playlistId, position);
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

        if (!playlist.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new PlaylistOwnershipException("You do not have permission to modify this playlist.");
        }

        PlaylistSong playlistSong = playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId)
                .orElseThrow(() -> new SongNotFoundException("The song is not in the playlist."));


        int position = playlistSong.getPosition();
        ;

        playlistSongRepository.delete(playlistSong);
        playlistSongRepository.flush();
        playlistSongRepository.shiftPositionsDown(playlistId, position);

    }

    public PageResponseDTO<PlaylistSongResponseDTO> findAllSongsPlaylist(UUID playlistId, PageRequestDTO pageRequest, CustomUserDetails userDetails) {

        Playlist playlist = playlistService.findById(playlistId);

        if (!playlist.isPublic() && !playlist.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new PlaylistNotFoundException("Playlist not found.");
        }

        Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), Sort.by("position").ascending());

        Page<PlaylistSongResponseDTO> allByPlaylistId = playlistSongRepository.findAllByPlaylistId(playlistId, pageable)
                .map(playlistSong -> new PlaylistSongResponseDTO(
                        playlistSong.getPosition(),
                        playlistSong.getCreatedAt(), songService.songResponse(playlistSong.getSong().getId()))
                );

        return PageResponseDTO.from(allByPlaylistId);
    }

    @Transactional
    public void changePosition(UUID playlistId, UUID userId, UUID songId, int newPosition) {

        Playlist playlist = playlistService.findById(playlistId);

        if (!playlist.getUser().getId().equals(userId)) {
            throw new PlaylistOwnershipException("You do not have permission to modify this playlist.");
        }

        PlaylistSong playlistSong = playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId)
                .orElseThrow(() -> new SongNotFoundException("The song is not in the playlist."));

        int total = (int) playlistSongRepository.countByPlaylistId(playlistId)+1;
        int oldPosition = playlistSong.getPosition();

        int target = Math.min(newPosition,total);

        if(newPosition == total){
            return;
        }

        if(target < oldPosition){
            playlistSongRepository.shiftRange(playlistId,target,oldPosition-1,+1);
        }
        else{
            playlistSongRepository.shiftRange(playlistId,oldPosition+1,target,-1);
        }

        playlistSong.setPosition(target);

    }
}