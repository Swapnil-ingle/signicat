package com.swapnil.signicat.service;

public interface RefreshTokenService {
    String generateRefreshToken();

    boolean validateRefreshToken(String refreshToken);

    void deleteRefreshToken(String refreshToken);
}
