package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    boolean existsByUserId(UUID userId);

    Optional<Artist> findByUserId(UUID userId);
}
