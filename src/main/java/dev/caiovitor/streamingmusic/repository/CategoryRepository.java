package dev.caiovitor.streamingmusic.repository;

import dev.caiovitor.streamingmusic.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);

    @Query("""
            SELECT COALESCE(MAX(u.position),0) FROM Category u
            """)
    int findMaxPosition();

    List<Category> findAllByActiveTrueOrderByPositionAscNameAsc();

    Optional<Category> findByIdAndActiveTrue(UUID categoryId);


    @Modifying
    @Query("""
            UPDATE Category u
            SET u.position = u.position + :shiftPosition
            WHERE u.position BETWEEN :from  AND  :to
            """)

    void shiftPosition(@Param("shiftPosition") int shiftPosition,
                       @Param("from") int from ,
                       @Param("to") int to);


    @Modifying
    @Query("""
            UPDATE Category u
            SET u.position= u.position - 1
            WHERE u.position > :position
            """)
    void shiftPositionDown(@Param("position") int position);
}
