package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.UserProfileResponseDTO;
import dev.caiovitor.streamingmusic.dto.UserProfileUpdateDTO;
import dev.caiovitor.streamingmusic.entity.User;
import dev.caiovitor.streamingmusic.exception.UserNotFoundException;
import dev.caiovitor.streamingmusic.mapper.UserMapper;
import dev.caiovitor.streamingmusic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User creatUser(User user ){
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public UserProfileResponseDTO getUserProfile(String email){
      User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new UserNotFoundException("User not found."));

        return userMapper.toDTO(user);

    }

    public void updateUserProfile(String email, UserProfileUpdateDTO userUpdate){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        userMapper.updateProfile(userUpdate,user);
        userRepository.save(user);
    }
}
