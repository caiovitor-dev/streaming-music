package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

   public Optional<Role> findByName(String name);


    Set<Role> findAllByNameIn(Set<String> name);
}
