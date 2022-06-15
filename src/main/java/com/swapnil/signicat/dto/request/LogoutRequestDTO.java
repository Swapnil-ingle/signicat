package com.swapnil.signicat.dto.request;

import lombok.Data;

@Data
public class LogoutRequestDTO {
    private String username;

    private String refreshToken;
}
