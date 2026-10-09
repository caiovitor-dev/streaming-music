package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.entity.Role;
import dev.caiovitor.streamingmusic.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findByName(String name){
        return roleRepository.findByName(name);
    }

    public Set<Role> findAllByName(Set<String> name){
        return roleRepository.findAllByNameIn(name);
    }
}
