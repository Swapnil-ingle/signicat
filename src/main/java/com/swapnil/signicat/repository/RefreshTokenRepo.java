package com.swapnil.signicat.repository;

import com.swapnil.signicat.security.RefreshToken;

public interface RefreshTokenRepo {
    void addRefreshToken(RefreshToken refreshToken);

    boolean isPresent(String refreshToken);

    void deleteRefreshToken(String refreshToken);
}
