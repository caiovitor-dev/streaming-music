package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    Page<Playlist> findAllByUserId(UUID id, Pageable pageable);

    Page<Playlist> findAllByNameContainingIgnoreCaseAndIsPublicTrue(String name,Pageable pageable);

    @Query("""
            SELECT p FROM Category c
            JOIN c.playlists p
            WHERE c.id = :categoryId
            AND p.isPublic = true
            """)
    Page<Playlist> findPublicByCategoryId(@Param("categoryId") UUID categoryId,Pageable pageable);
}
