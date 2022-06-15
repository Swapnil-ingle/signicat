package com.swapnil.signicat.service.impl;

import com.swapnil.signicat.dto.response.AuthResponseDTO;
import com.swapnil.signicat.dto.request.LoginRequestDTO;
import com.swapnil.signicat.dto.request.RegisterRequestDTO;
import com.swapnil.signicat.dto.SubjectDTO;
import com.swapnil.signicat.model.Subject;
import com.swapnil.signicat.repository.UserRepository;
import com.swapnil.signicat.security.JwtProvider;
import com.swapnil.signicat.service.AuthService;
import com.swapnil.signicat.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public SubjectDTO register(RegisterRequestDTO registerRequestDTO) {
        Subject user = Subject.builder()
                .username(registerRequestDTO.getUsername())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .build();

        Subject savedUser = userRepository.save(user);

        return SubjectDTO.from(savedUser);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        return AuthResponseDTO.builder()
                .token(token)
                .username(loginRequestDTO.getUsername())
                .expiresAt(Instant.now().plus(jwtProvider.getExpirationTimeInMins(), ChronoUnit.MINUTES))
                .refreshToken(refreshTokenService.generateRefreshToken())
                .build();
    }
}
