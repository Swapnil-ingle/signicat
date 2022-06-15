package com.swapnil.signicat.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequestDTO {
    private String username;

    private String password;
}
