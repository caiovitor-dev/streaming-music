package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SongRepository extends JpaRepository<Song, UUID> , JpaSpecificationExecutor<Song> {
}
