package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {
}
