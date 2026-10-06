package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
}
