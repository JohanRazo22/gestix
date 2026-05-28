package com.gestix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestix.config.PasswordEncoderConfig;
import com.gestix.config.SecurityConfig;
import com.gestix.dto.UpdateProfileRequest;
import com.gestix.dto.UserProfileResponse;
import com.gestix.security.CustomOAuth2UserService;
import com.gestix.security.CustomOidcUserService;
import com.gestix.security.CustomUserDetailsService;
import com.gestix.security.JwtAuthFilter;
import com.gestix.security.JwtUtil;
import com.gestix.security.OAuth2AuthenticationSuccessHandler;
import com.gestix.service.UserService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({ SecurityConfig.class, PasswordEncoderConfig.class })
@DisplayName("UserController - Pruebas de integración (MockMvc)")
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean UserService userService;
    @MockBean JwtAuthFilter jwtAuthFilter;
    @MockBean JwtUtil jwtUtil;
    @MockBean CustomUserDetailsService userDetailsService;
    @MockBean CustomOAuth2UserService oAuth2UserService;
    @MockBean CustomOidcUserService oidcUserService;
    @MockBean OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;

    private UserProfileResponse sampleProfile;

    @BeforeEach
    void setUp() throws Exception {
        doAnswer(inv -> {
            HttpServletRequest req = inv.getArgument(0);
            HttpServletResponse res = inv.getArgument(1);
            FilterChain chain = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());

        sampleProfile = UserProfileResponse.builder()
                .username("johndoe")
                .email("john@example.com")
                .build();
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("GET /users/me: devuelve perfil")
    void getProfile_returns200() throws Exception {
        when(userService.getProfile("john@example.com")).thenReturn(sampleProfile);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("PUT /users/me: actualiza username")
    void updateProfile_returns200() throws Exception {
        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setUsername("johnny");

        UserProfileResponse updated = UserProfileResponse.builder()
                .username("johnny")
                .email("john@example.com")
                .build();

        when(userService.updateProfile(any(UpdateProfileRequest.class), eq("john@example.com")))
                .thenReturn(updated);

        mockMvc.perform(put("/api/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johnny"));
    }
}
