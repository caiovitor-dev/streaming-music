package dev.caiovitor.streamingmusic.service;

import dev.caiovitor.streamingmusic.entity.RefreshToken;
import dev.caiovitor.streamingmusic.entity.User;
import dev.caiovitor.streamingmusic.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private Duration refreshTokenExpiration;

    public String createRefreshToken(User user){

        RefreshToken refreshToken = new RefreshToken();
        String token = UUID.randomUUID().toString();


        refreshToken.setExpiresAt(LocalDateTime.now().plus(refreshTokenExpiration));
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hashToken(token));

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    private String hashToken(String token){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algorithm not found");
        }
    }


}
