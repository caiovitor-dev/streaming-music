package dev.caiovitor.streamingmusic.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name",nullable = false,unique = true,length = 50)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

}
