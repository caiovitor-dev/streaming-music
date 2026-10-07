package dev.caiovitor.streamingmusic.service;


import dev.caiovitor.streamingmusic.dto.RefreshTokenResult;
import dev.caiovitor.streamingmusic.entity.RefreshToken;
import dev.caiovitor.streamingmusic.entity.User;
import dev.caiovitor.streamingmusic.exception.TokenExpiredException;
import dev.caiovitor.streamingmusic.exception.TokenNotFoundException;
import dev.caiovitor.streamingmusic.exception.TokenRevokedException;
import dev.caiovitor.streamingmusic.repository.RefreshTokenRepository;
import dev.caiovitor.streamingmusic.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

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

    public RefreshTokenResult rotateRefreshToken(String token){

        RefreshToken oldToken = findByTokenHash(token)
               .orElseThrow(() -> new TokenNotFoundException("Token not found."));

        if(oldToken.getExpiresAt().isBefore(LocalDateTime.now())){

           oldToken.setRevokedAt(LocalDateTime.now());
           refreshTokenRepository.save(oldToken);

           throw new TokenExpiredException("Log again.");
        }

        if(oldToken.getRevokedAt() != null){
           throw new TokenRevokedException("Token revoked");
        }

        oldToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(oldToken);

        String newAccess = jwtService.generateAccessToken(oldToken.getUser());
        String refreshToken = createRefreshToken(oldToken.getUser());

        return new RefreshTokenResult(newAccess, refreshToken);
    }



    private Optional<RefreshToken> findByTokenHash(String token){
        return refreshTokenRepository.findByTokenHash(hashToken(token));
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
