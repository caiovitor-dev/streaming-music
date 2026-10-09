package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.PageResponseDTO;
import dev.caiovitor.streamingmusic.dto.UserProfileResponseDTO;
import dev.caiovitor.streamingmusic.dto.UserProfileUpdateDTO;
import dev.caiovitor.streamingmusic.entity.Role;
import dev.caiovitor.streamingmusic.entity.User;
import dev.caiovitor.streamingmusic.exception.RoleNotFoundException;
import dev.caiovitor.streamingmusic.exception.UserNotFoundException;
import dev.caiovitor.streamingmusic.mapper.UserMapper;
import dev.caiovitor.streamingmusic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.UUID;



@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;

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

    public PageResponseDTO<UserProfileResponseDTO> findAllUsers(int page,int size){

        Pageable pageable  = PageRequest.of(page,size);
        Page<UserProfileResponseDTO> content = userRepository.findAll(pageable).map(userMapper::toDTO);

        return PageResponseDTO.from(content);
    }

    @Transactional
    public void updateUserRole(UUID id, Set<String> roleNames){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Set<Role> roles = roleService.findAllByName(roleNames);

        if(roles.size() != roleNames.size()){
            throw new RoleNotFoundException("One or more specified functions were not found.");
        }

        user.setRoles(roles);
        userRepository.save(user);

    }
}
