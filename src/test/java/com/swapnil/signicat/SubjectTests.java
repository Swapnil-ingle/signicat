package com.swapnil.signicat;

import com.swapnil.signicat.dto.SubjectDTO;
import com.swapnil.signicat.dto.request.LoginRequestDTO;
import com.swapnil.signicat.dto.request.RegisterRequestDTO;
import com.swapnil.signicat.dto.response.AuthResponseDTO;
import com.swapnil.signicat.model.Subject;
import com.swapnil.signicat.repository.UserRepository;
import com.swapnil.signicat.security.JwtProvider;
import com.swapnil.signicat.service.RefreshTokenService;
import com.swapnil.signicat.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.swapnil.signicat.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class SubjectTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void init() {
        when(userRepository.save(any(Subject.class))).thenReturn(getDemoUser());
        when(passwordEncoder.encode(anyString())).thenReturn(DEMO_ENCODED_PASSWORD);

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtProvider.generateToken(any())).thenReturn(DEMO_JWT_TOKEN);
        when(refreshTokenService.generateRefreshToken()).thenReturn(DEMO_REFRESH_TOKEN);
    }

    @Test
    public void registerNewUserTest() {
        SubjectDTO subjectDTO = authService.register(getDemoRegisterRequestDTO());
        assertEquals(subjectDTO.getId(), subjectDTO.getId());
    }

    @Test
    public void loginWithUser() {
        AuthResponseDTO authResponseDTO = authService.login(getDemoLoginReqDTO());
        assertEquals(authResponseDTO.getUsername(), DEMO_USER_NAME);
    }

    private LoginRequestDTO getDemoLoginReqDTO() {
        return LoginRequestDTO.builder().username(DEMO_USER_NAME).password(DEMO_PASSWORD).build();
    }

    private RegisterRequestDTO getDemoRegisterRequestDTO() {
        return RegisterRequestDTO.builder()
                .username(DEMO_USER_NAME)
                .password(DEMO_PASSWORD)
                .build();
    }
}
