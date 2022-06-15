package com.swapnil.signicat.dto.request;

import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
    private String refreshToken;

    private String username;
}
