package com.swapnil.signicat.controller;

import com.swapnil.signicat.dto.*;
import com.swapnil.signicat.dto.request.LoginRequestDTO;
import com.swapnil.signicat.dto.request.LogoutRequestDTO;
import com.swapnil.signicat.dto.request.RefreshTokenRequestDTO;
import com.swapnil.signicat.dto.request.RegisterRequestDTO;
import com.swapnil.signicat.dto.response.AuthResponseDTO;
import com.swapnil.signicat.security.JwtProvider;
import com.swapnil.signicat.service.AuthService;
import com.swapnil.signicat.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    private final JwtProvider jwtProvider;

    @PostMapping("/register")
    public SubjectDTO registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return authService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

    @PostMapping("/refresh/token")
    public AuthResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return jwtProvider.refreshJwtToken(refreshTokenRequestDTO);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        refreshTokenService.deleteRefreshToken(logoutRequestDTO.getRefreshToken());
    }
}
