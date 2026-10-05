package dev.caiovitor.streamingmusic.security;

import dev.caiovitor.streamingmusic.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


@Component
public class JwtService {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;


    private SecretKey getSigningKey(){
        byte[] keyBytes= Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user){

        Map<String,Object> claims = new HashMap<>();

        claims.put("id",user.getId());
        claims.put("name",user.getName());

        return createToken(claims,user.getEmail());

    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public UUID extractId(String token){
        return extractClaim(token,claims -> claims.get("id", UUID.class));
    }

    public String extractName(String token){
        return extractClaim(token,claims -> claims.get("name", String.class));
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){

        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token,String email){
        return extractUsername(token).equals(email) && !isTokenExpired(token);
    }
}
