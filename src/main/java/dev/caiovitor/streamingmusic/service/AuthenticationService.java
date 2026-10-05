package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.UserCreateDTO;
import dev.caiovitor.streamingmusic.entity.Role;
import dev.caiovitor.streamingmusic.entity.User;
import dev.caiovitor.streamingmusic.exception.EmailAlreadyExistsException;
import dev.caiovitor.streamingmusic.exception.RoleNotFoundException;
import dev.caiovitor.streamingmusic.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthenticationService {


    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;


    public User registerUser(UserCreateDTO userRegister){

        if(userService.existsByEmail(userRegister.email())){
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        String encode = passwordEncoder.encode(userRegister.password());
        Role role = roleService.findByName("ROLE_USER").orElseThrow(() -> new RoleNotFoundException("Role Not Found")) ;

        User user = userMapper.toEntity(userRegister);
        user.setRoles(Set.of(role));
        user.setPassword(encode);

        return userService.creatUser(user);
    }


}
