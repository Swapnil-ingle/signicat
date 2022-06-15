package com.swapnil.signicat.security;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RefreshToken {
    private String refreshToken;

    private Instant issuedAt;
}
