package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SongRepository extends JpaRepository<Song, UUID> , JpaSpecificationExecutor<Song> {

    @Override
    @EntityGraph(attributePaths = "artist")
    Page<Song> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "artist")
    Page<Song> findAll(Specification<Song> spec, Pageable pageable);
}
