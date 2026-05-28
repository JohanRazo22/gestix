package com.gestix.service;

import com.gestix.dto.AuthResponse;
import com.gestix.dto.LoginRequest;
import com.gestix.dto.RegisterRequest;
import com.gestix.entity.User;
import com.gestix.repository.UserRepository;
import com.gestix.security.CustomUserDetailsService;
import com.gestix.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Pruebas unitarias")
class AuthServiceTest {

    @Mock private UserRepository       userRepository;
    @Mock private PasswordEncoder      passwordEncoder;
    @Mock private JwtUtil              jwtUtil;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    private User sampleUser;
    private UserDetails sampleUserDetails;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .password("HASHED_PASSWORD")
                .build();

        sampleUserDetails = org.springframework.security.core.userdetails.User
                .withUsername("john@example.com")
                .password("HASHED_PASSWORD")
                .authorities(Collections.emptyList())
                .build();
    }

    // ══════════════════ REGISTER ══════════════════

    @Test
    @DisplayName("register: éxito - devuelve token y datos del usuario")
    void register_success_returnsAuthResponse() {
        RegisterRequest req = buildRegisterRequest("johndoe", "john@example.com", "secret123");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("johndoe")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("HASHED_PASSWORD");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(userDetailsService.loadUserByUsername("john@example.com")).thenReturn(sampleUserDetails);
        when(jwtUtil.generateToken(sampleUserDetails)).thenReturn("jwt-token-123");

        AuthResponse response = authService.register(req);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt-token-123");
        assertThat(response.getUsername()).isEqualTo("johndoe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("secret123");
        verify(jwtUtil).generateToken(sampleUserDetails);
    }

    @Test
    @DisplayName("register: email duplicado - lanza IllegalArgumentException")
    void register_duplicateEmail_throwsException() {
        RegisterRequest req = buildRegisterRequest("johndoe", "dup@example.com", "secret123");
        when(userRepository.existsByEmail("dup@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register: username duplicado - lanza IllegalArgumentException")
    void register_duplicateUsername_throwsException() {
        RegisterRequest req = buildRegisterRequest("taken", "new@example.com", "secret123");
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("usuario");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register: contraseña se encripta con BCrypt antes de guardar")
    void register_passwordIsEncrypted() {
        RegisterRequest req = buildRegisterRequest("johndoe", "john@example.com", "plaintext");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(passwordEncoder.encode("plaintext")).thenReturn("$2a$ENCRYPTED");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(sampleUserDetails);
        when(jwtUtil.generateToken(any())).thenReturn("token");

        authService.register(req);

        verify(passwordEncoder).encode("plaintext");
        verify(userRepository).save(argThat(u -> u.getPassword().equals("$2a$ENCRYPTED")));
    }

    // ══════════════════ LOGIN ══════════════════

    @Test
    @DisplayName("login: éxito - autentica y devuelve token")
    void login_success_returnsAuthResponse() {
        LoginRequest req = buildLoginRequest("john@example.com", "secret123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("john@example.com", "secret123"));
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleUser));
        when(userDetailsService.loadUserByUsername("john@example.com")).thenReturn(sampleUserDetails);
        when(jwtUtil.generateToken(sampleUserDetails)).thenReturn("login-jwt-token");

        AuthResponse response = authService.login(req);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("login-jwt-token");
        assertThat(response.getUsername()).isEqualTo("johndoe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("login: usuario no existe en BD - lanza IllegalArgumentException")
    void login_userNotFound_throwsException() {
        LoginRequest req = buildLoginRequest("nobody@example.com", "secret");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no encontrado");
    }

    @Test
    @DisplayName("login: credenciales incorrectas - AuthenticationManager lanza BadCredentialsException")
    void login_badCredentials_throwsException() {
        LoginRequest req = buildLoginRequest("john@example.com", "wrongpassword");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciales invalidas"));

        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(BadCredentialsException.class);
    }

    // ══════════════════ Helpers ══════════════════

    private RegisterRequest buildRegisterRequest(String username, String email, String password) {
        RegisterRequest r = new RegisterRequest();
        r.setUsername(username);
        r.setEmail(email);
        r.setPassword(password);
        return r;
    }

    private LoginRequest buildLoginRequest(String email, String password) {
        LoginRequest r = new LoginRequest();
        r.setEmail(email);
        r.setPassword(password);
        return r;
    }
}
