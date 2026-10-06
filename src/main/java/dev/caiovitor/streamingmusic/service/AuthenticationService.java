package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.dto.LoginRequestDTO;
import dev.caiovitor.streamingmusic.dto.LoginResponseDTO;
import dev.caiovitor.streamingmusic.dto.UserCreateDTO;
import dev.caiovitor.streamingmusic.entity.Role;
import dev.caiovitor.streamingmusic.entity.User;
import dev.caiovitor.streamingmusic.exception.EmailAlreadyExistsException;
import dev.caiovitor.streamingmusic.exception.RoleNotFoundException;
import dev.caiovitor.streamingmusic.mapper.UserMapper;
import dev.caiovitor.streamingmusic.security.CustomUserDetails;
import dev.caiovitor.streamingmusic.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


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

    public LoginResponseDTO login(LoginRequestDTO loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponseDTO(accessToken,refreshToken);

    }


}
