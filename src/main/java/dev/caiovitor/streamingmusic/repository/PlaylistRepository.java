package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.Playlist;
import dev.caiovitor.streamingmusic.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    Page<Playlist> findAllByUserId(UUID id, Pageable pageable);
}
