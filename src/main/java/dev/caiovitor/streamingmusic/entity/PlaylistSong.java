package dev.caiovitor.streamingmusic.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "playlist_songs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class PlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id",nullable = false)
    private Playlist playlist;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id",nullable = false)
    private Song song;

    @CreatedDate
    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updated_at;

    @Column(name = "position",nullable = false)
    private Integer position;
}
