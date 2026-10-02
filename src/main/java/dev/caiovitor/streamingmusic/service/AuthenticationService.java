package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.entity.Role;
import dev.caiovitor.streamingmusic.entity.User;
import dev.caiovitor.streamingmusic.exception.EmailAlreadyExistsException;
import dev.caiovitor.streamingmusic.repository.RoleRepository;
import dev.caiovitor.streamingmusic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthenticationService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User registerUser(User user){

        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        String encode = passwordEncoder.encode(user.getPassword());
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("")) ;

        user.setRoles(Set.of(role));
        user.setPassword(encode);

        return userRepository.save(user);
    }
}
