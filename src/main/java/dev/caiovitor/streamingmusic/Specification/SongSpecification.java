package dev.caiovitor.streamingmusic.Specification;

import dev.caiovitor.streamingmusic.entity.Song;
import org.springframework.data.jpa.domain.Specification;

public class SongSpecification {

    public static Specification<Song> titleContains(String title){

        if(title == null || title.isBlank()){
            return (root, query, cb) -> cb.conjunction();
        }

        return (root, query, cb) ->
                cb.like(cb.upper(root.get("title")),"%" + title.toUpperCase() + "%");
    }

    public static Specification<Song> artistContains(String artist){

        if(artist ==null || artist.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

        return (root, query, cb) ->
                cb.like(cb.upper(root.get("artist")),"%" + artist.toUpperCase() + "%");

    }
}
