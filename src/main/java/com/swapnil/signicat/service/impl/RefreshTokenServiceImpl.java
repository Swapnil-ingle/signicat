package com.swapnil.signicat.service.impl;

import com.swapnil.signicat.exception.RefreshTokenMissingException;
import com.swapnil.signicat.repository.RefreshTokenRepo;
import com.swapnil.signicat.security.RefreshToken;
import com.swapnil.signicat.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;

    @Override
    public String generateRefreshToken() {
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString())
                .issuedAt(Instant.now())
                .build();

        refreshTokenRepo.addRefreshToken(refreshToken);
        return refreshToken.getRefreshToken();
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        if (!refreshTokenRepo.isPresent(refreshToken)) {
            throw new RefreshTokenMissingException(refreshToken);
        }

        return true;
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepo.deleteRefreshToken(refreshToken);
    }
}
