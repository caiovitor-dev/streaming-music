package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCase(String name);

    @Query("""
            SELECT COALESCE(MAX(u.position),0) FROM Category u
            """)
    int findMaxPosition();

    List<Category> findAllByActiveTrueOrderByPositionAscNameAsc();

}
