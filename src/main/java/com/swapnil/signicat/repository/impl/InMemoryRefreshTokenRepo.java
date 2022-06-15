package com.swapnil.signicat.repository.impl;

import com.swapnil.signicat.exception.RefreshTokenAlreadyExistException;
import com.swapnil.signicat.repository.RefreshTokenRepo;
import com.swapnil.signicat.security.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Transactional
@Repository
@Slf4j
public class InMemoryRefreshTokenRepo implements RefreshTokenRepo {

    private Map<String, RefreshToken> refreshTokens = new HashMap<>();

    @Override
    public void addRefreshToken(RefreshToken refreshToken) {
        if (refreshTokens.containsKey(refreshToken.getRefreshToken())) {
            log.error("Cannot add a new refresh token: {}", refreshToken);
            throw new RefreshTokenAlreadyExistException(refreshToken.getRefreshToken());
        }

        refreshTokens.put(refreshToken.getRefreshToken(), refreshToken);
    }

    @Override
    public boolean isPresent(String refreshToken) {
        return refreshTokens.containsKey(refreshToken);
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        refreshTokens.remove(refreshToken);
    }
}
