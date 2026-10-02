package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

}
