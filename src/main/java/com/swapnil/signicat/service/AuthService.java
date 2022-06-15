package com.swapnil.signicat.service;

import com.swapnil.signicat.dto.response.AuthResponseDTO;
import com.swapnil.signicat.dto.request.LoginRequestDTO;
import com.swapnil.signicat.dto.request.RegisterRequestDTO;
import com.swapnil.signicat.dto.SubjectDTO;

public interface AuthService {
    SubjectDTO register(RegisterRequestDTO registerRequestDTO);

    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
}
