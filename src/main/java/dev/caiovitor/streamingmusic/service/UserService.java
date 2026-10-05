package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.entity.User;
import dev.caiovitor.streamingmusic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User creatUser(User user ){
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
