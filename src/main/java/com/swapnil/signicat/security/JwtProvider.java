package com.swapnil.signicat.security;

import com.swapnil.signicat.dto.response.AuthResponseDTO;
import com.swapnil.signicat.dto.request.RefreshTokenRequestDTO;
import com.swapnil.signicat.exception.UserRequiredForGeneratingTokenClaimsException;
import com.swapnil.signicat.model.Subject;
import com.swapnil.signicat.service.RefreshTokenService;
import com.swapnil.signicat.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class JwtProvider {
    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Value("${jwt.secret.key}")
    private String key;

    @Value("${jwt.expiration.time.in.minutes}")
    private Integer expirationTimeInMins;

    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return generateTokenFromUsername(userDetails.getUsername());
    }

    public AuthResponseDTO refreshJwtToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        refreshTokenService.validateRefreshToken(refreshTokenRequestDTO.getRefreshToken());
        String newJwtToken = generateTokenFromUsername(refreshTokenRequestDTO.getUsername());

        return AuthResponseDTO.builder()
                .token(newJwtToken)
                .refreshToken(refreshTokenRequestDTO.getRefreshToken())
                .username(refreshTokenRequestDTO.getUsername())
                .expiresAt(Instant.now().plus(expirationTimeInMins, ChronoUnit.MINUTES))
                .build();
    }

    public String generateTokenFromUsername(String username) {
        Optional<Subject> user = userService.getByUsername(username);

        if (user.isEmpty()) {
            log.error("Cannot generate token as user not found (username: '{}')", username);
            throw new UserRequiredForGeneratingTokenClaimsException(username);
        }

        return Jwts.builder()
                .setSubject(user.get().getId().toString())
                .setClaims(userService.getClaims(user.get()))
                .signWith(Keys.hmacShaKeyFor(key.getBytes()), SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(expirationTimeInMins)))
                .compact();
    }

    public boolean validateToken(String jwtToken) {
        Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                .build()
                .parse(jwtToken);

        return true;
    }

    public String getUsernameFromJwt(String jwtToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                .build()
                .parseClaimsJws(jwtToken).getBody();

        return claims.getSubject();
    }

    public Integer getExpirationTimeInMins() {
        return expirationTimeInMins;
    }
}
