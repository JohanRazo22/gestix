package com.gestix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestix.config.PasswordEncoderConfig;
import com.gestix.config.SecurityConfig;
import com.gestix.dto.ProjectRequest;
import com.gestix.dto.ProjectResponse;
import com.gestix.security.CustomOAuth2UserService;
import com.gestix.security.CustomOidcUserService;
import com.gestix.security.CustomUserDetailsService;
import com.gestix.security.JwtAuthFilter;
import com.gestix.security.JwtUtil;
import com.gestix.security.OAuth2AuthenticationSuccessHandler;
import com.gestix.service.ProjectService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@Import({ SecurityConfig.class, PasswordEncoderConfig.class })
@DisplayName("ProjectController - Pruebas de integración (MockMvc)")
class ProjectControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean ProjectService projectService;
    @MockBean JwtAuthFilter jwtAuthFilter;
    @MockBean JwtUtil jwtUtil;
    @MockBean CustomUserDetailsService userDetailsService;
    @MockBean CustomOAuth2UserService oAuth2UserService;
    @MockBean CustomOidcUserService oidcUserService;
    @MockBean OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;

    private ProjectResponse sampleResponse;

    @BeforeEach
    void setUp() throws Exception {
        doAnswer(inv -> {
            HttpServletRequest req = inv.getArgument(0);
            HttpServletResponse res = inv.getArgument(1);
            FilterChain chain = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());

        sampleResponse = ProjectResponse.builder()
                .id(1L)
                .name("Marketing")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("GET /projects: devuelve lista")
    void getMyProjects_returnsList() throws Exception {
        when(projectService.getMyProjects("john@example.com")).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Marketing"));
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("POST /projects: crea proyecto")
    void createProject_returns200() throws Exception {
        ProjectRequest req = new ProjectRequest();
        req.setName("Marketing");

        when(projectService.create(any(ProjectRequest.class), eq("john@example.com")))
                .thenReturn(sampleResponse);

        mockMvc.perform(post("/api/projects")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Marketing"));
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("DELETE /projects/{id}: devuelve 204")
    void deleteProject_returns204() throws Exception {
        doNothing().when(projectService).delete(1L, "john@example.com");

        mockMvc.perform(delete("/api/projects/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(projectService).delete(1L, "john@example.com");
    }
}
