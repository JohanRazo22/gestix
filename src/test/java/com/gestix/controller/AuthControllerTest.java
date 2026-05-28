package com.gestix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestix.dto.AuthResponse;
import com.gestix.dto.ForgotPasswordRequest;
import com.gestix.dto.ForgotPasswordResponse;
import com.gestix.dto.LoginRequest;
import com.gestix.dto.MessageResponse;
import com.gestix.dto.RegisterRequest;
import com.gestix.dto.ResetPasswordRequest;
import com.gestix.config.PasswordEncoderConfig;
import com.gestix.config.SecurityConfig;
import com.gestix.security.CustomOAuth2UserService;
import com.gestix.security.CustomOidcUserService;
import com.gestix.security.CustomUserDetailsService;
import com.gestix.security.JwtAuthFilter;
import com.gestix.security.JwtUtil;
import com.gestix.security.OAuth2AuthenticationSuccessHandler;
import com.gestix.service.AuthService;
import com.gestix.service.PasswordResetService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({ SecurityConfig.class, PasswordEncoderConfig.class })
@DisplayName("AuthController - Pruebas de integración (MockMvc)")
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean AuthService                         authService;
    @MockBean PasswordResetService               passwordResetService;
    @MockBean JwtAuthFilter                       jwtAuthFilter;
    @MockBean JwtUtil                             jwtUtil;
    @MockBean CustomUserDetailsService            userDetailsService;
    @MockBean CustomOAuth2UserService             oAuth2UserService;
    @MockBean CustomOidcUserService               oidcUserService;
    @MockBean OAuth2AuthenticationSuccessHandler  oAuth2SuccessHandler;

    @BeforeEach
    void setUpFilter() throws Exception {
        // El mock del filtro debe propagar la cadena para no bloquear las peticiones
        doAnswer(inv -> {
            HttpServletRequest  req   = inv.getArgument(0);
            HttpServletResponse res   = inv.getArgument(1);
            FilterChain         chain = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());
    }

    // ══════════════════ POST /api/auth/register ══════════════════

    @Test
    @DisplayName("POST /register: éxito - devuelve 200 + token y username")
    void register_validRequest_returns200() throws Exception {
        RegisterRequest req = buildRegisterReq("johndoe", "john@example.com", "secret123");
        AuthResponse resp = AuthResponse.builder()
                .token("jwt-token").username("johndoe").email("john@example.com").build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("POST /register: email inválido - devuelve 400")
    void register_invalidEmail_returns400() throws Exception {
        RegisterRequest req = buildRegisterReq("johndoe", "not-an-email", "secret123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /register: username demasiado corto - devuelve 400")
    void register_shortUsername_returns400() throws Exception {
        RegisterRequest req = buildRegisterReq("ab", "john@example.com", "secret123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /register: contraseña menor a 6 caracteres - devuelve 400")
    void register_shortPassword_returns400() throws Exception {
        RegisterRequest req = buildRegisterReq("johndoe", "john@example.com", "abc");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /register: email duplicado - servicio lanza excepción - devuelve 400")
    void register_duplicateEmail_returns400() throws Exception {
        RegisterRequest req = buildRegisterReq("johndoe", "dup@example.com", "secret123");

        when(authService.register(any())).thenThrow(
                new IllegalArgumentException("El email ya esta registrado"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El email ya esta registrado"));
    }

    @Test
    @DisplayName("POST /register: body vacío - devuelve 400")
    void register_emptyBody_returns400() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ══════════════════ POST /api/auth/login ══════════════════

    @Test
    @DisplayName("POST /login: éxito - devuelve 200 + token")
    void login_validRequest_returns200() throws Exception {
        LoginRequest req = buildLoginReq("john@example.com", "secret123");
        AuthResponse resp = AuthResponse.builder()
                .token("login-token").username("johndoe").email("john@example.com").build();

        when(authService.login(any(LoginRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("login-token"))
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    @DisplayName("POST /login: email inválido - devuelve 400")
    void login_invalidEmail_returns400() throws Exception {
        LoginRequest req = buildLoginReq("not-an-email", "secret123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /login: contraseña vacía - devuelve 400")
    void login_emptyPassword_returns400() throws Exception {
        LoginRequest req = buildLoginReq("john@example.com", "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /login: credenciales incorrectas - servicio lanza excepción - devuelve 400")
    void login_badCredentials_returns400() throws Exception {
        LoginRequest req = buildLoginReq("john@example.com", "wrongpass");

        when(authService.login(any())).thenThrow(
                new IllegalArgumentException("Credenciales incorrectas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Credenciales incorrectas"));
    }

    // ══════════════════ POST /api/auth/forgot-password ══════════════════

    @Test
    @DisplayName("POST /forgot-password: éxito - devuelve 200 + mensaje")
    void forgotPassword_validRequest_returns200() throws Exception {
        ForgotPasswordRequest req = new ForgotPasswordRequest();
        req.setEmail("john@example.com");

        when(passwordResetService.requestReset("john@example.com"))
                .thenReturn(ForgotPasswordResponse.builder()
                        .message("Si el correo esta registrado, recibiras un enlace.")
                        .build());

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /forgot-password: email inválido - devuelve 400")
    void forgotPassword_invalidEmail_returns400() throws Exception {
        ForgotPasswordRequest req = new ForgotPasswordRequest();
        req.setEmail("not-an-email");

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ══════════════════ POST /api/auth/reset-password ══════════════════

    @Test
    @DisplayName("POST /reset-password: éxito - devuelve 200 + mensaje")
    void resetPassword_validRequest_returns200() throws Exception {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setToken("abc-123");
        req.setPassword("secret123");

        when(passwordResetService.resetPassword("abc-123", "secret123"))
                .thenReturn(MessageResponse.of("Contrasena actualizada correctamente."));

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contrasena actualizada correctamente."));
    }

    @Test
    @DisplayName("POST /reset-password: contraseña corta - devuelve 400")
    void resetPassword_shortPassword_returns400() throws Exception {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setToken("abc-123");
        req.setPassword("abc");

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ══════════════════ Helpers ══════════════════

    private RegisterRequest buildRegisterReq(String username, String email, String password) {
        RegisterRequest r = new RegisterRequest();
        r.setUsername(username); r.setEmail(email); r.setPassword(password);
        return r;
    }

    private LoginRequest buildLoginReq(String email, String password) {
        LoginRequest r = new LoginRequest();
        r.setEmail(email); r.setPassword(password);
        return r;
    }
}
