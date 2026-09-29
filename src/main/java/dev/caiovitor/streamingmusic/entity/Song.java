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
@Table(name = "songs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "title",nullable = false,length = 100)
    private String title;

    @Column(name = "artist",nullable = false,length = 80)
    private String artist;

    @Column(name = "song_url",nullable = false,columnDefinition = "TEXT")
    private String songUrl;

    @Column(name = "image_url",nullable = false,columnDefinition = "TEXT")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updated_at;
}
