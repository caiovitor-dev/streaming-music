package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    @Query("""
           SELECT DISTINCT u 
           FROM User u 
           LEFT JOIN FETCH u.roles
            WHERE u.email =:email
           """)
    public Optional<User> findByEmailWithRoles(@Param("email")String email);
}
