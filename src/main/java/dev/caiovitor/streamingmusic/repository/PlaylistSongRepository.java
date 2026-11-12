package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, UUID> {



    boolean existsByPlaylistIdAndSongId(UUID playlistId,UUID songId);

    @Modifying
    @Query("""
            UPDATE PlaylistSong u
            SET u.position = u.position + 1
            WHERE u.playlist.id = :id
            AND u.position >= :position
            """)

    void changePosition(@Param("id") UUID playlistId, @Param("position") int position);


    @Modifying
    @Query("""
            UPDATE PlaylistSong u
            SET u.position = u.position - 1
            WHERE u.playlist.id = :id
            AND u.position > :position
            """)

    void shiftPositionsDown(@Param("id") UUID playlistId, @Param("position") int position);



    long countByPlaylistId(UUID id);

    Optional<PlaylistSong> findByPlaylistIdAndSongId(UUID playlistId, UUID songId);

}
