package com.gestix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestix.dto.TaskRequest;
import com.gestix.dto.TaskResponse;
import com.gestix.enums.Priority;
import com.gestix.enums.TaskStatus;
import com.gestix.config.PasswordEncoderConfig;
import com.gestix.config.SecurityConfig;
import com.gestix.security.CustomOAuth2UserService;
import com.gestix.security.CustomOidcUserService;
import com.gestix.security.CustomUserDetailsService;
import com.gestix.security.JwtAuthFilter;
import com.gestix.security.JwtUtil;
import com.gestix.security.OAuth2AuthenticationSuccessHandler;
import com.gestix.service.TaskService;
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

@WebMvcTest(TaskController.class)
@Import({ SecurityConfig.class, PasswordEncoderConfig.class })
@DisplayName("TaskController - Pruebas de integración (MockMvc)")
class TaskControllerTest {

    @Autowired MockMvc       mockMvc;
    @Autowired ObjectMapper  objectMapper;

    @MockBean TaskService                         taskService;
    @MockBean JwtAuthFilter                       jwtAuthFilter;
    @MockBean JwtUtil                             jwtUtil;
    @MockBean CustomUserDetailsService            userDetailsService;
    @MockBean CustomOAuth2UserService             oAuth2UserService;
    @MockBean CustomOidcUserService               oidcUserService;
    @MockBean OAuth2AuthenticationSuccessHandler  oAuth2SuccessHandler;

    private TaskResponse sampleResponse;

    @BeforeEach
    void setUp() throws Exception {
        // El mock del filtro propaga la cadena para no bloquear peticiones
        doAnswer(inv -> {
            HttpServletRequest  req   = inv.getArgument(0);
            HttpServletResponse res   = inv.getArgument(1);
            FilterChain         chain = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());

        sampleResponse = TaskResponse.builder()
                .id(1L)
                .title("Tarea de prueba")
                .description("Descripcion")
                .status(TaskStatus.PENDING)
                .priority(Priority.HIGH)
                .category("Trabajo")
                .ownerUsername("johndoe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ══════════════════ POST /api/tasks ══════════════════

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("POST /tasks: éxito - devuelve 200 + TaskResponse")
    void createTask_authenticated_returns200() throws Exception {
        TaskRequest req = buildTaskRequest("Tarea de prueba", TaskStatus.PENDING, Priority.HIGH);

        when(taskService.create(any(TaskRequest.class), eq("john@example.com")))
                .thenReturn(sampleResponse);

        mockMvc.perform(post("/api/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Tarea de prueba"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.ownerUsername").value("johndoe"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /tasks: título vacío - devuelve 400 por validación")
    void createTask_emptyTitle_returns400() throws Exception {
        TaskRequest req = buildTaskRequest("", TaskStatus.PENDING, Priority.MEDIUM);

        mockMvc.perform(post("/api/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /tasks: sin autenticación - devuelve 401 o 403")
    void createTask_unauthenticated_returns401or403() throws Exception {
        TaskRequest req = buildTaskRequest("Tarea", TaskStatus.PENDING, Priority.LOW);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is4xxClientError());
    }

    // ══════════════════ GET /api/tasks ══════════════════

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("GET /tasks: éxito - devuelve lista de tareas")
    void getMyTasks_authenticated_returnsList() throws Exception {
        TaskResponse task2 = TaskResponse.builder()
                .id(2L).title("Segunda tarea").status(TaskStatus.IN_PROGRESS)
                .priority(Priority.LOW).ownerUsername("johndoe")
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(taskService.getMyTasks("john@example.com"))
                .thenReturn(List.of(sampleResponse, task2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Tarea de prueba"))
                .andExpect(jsonPath("$[1].title").value("Segunda tarea"));
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("GET /tasks: lista vacía - devuelve array vacío")
    void getMyTasks_empty_returnsEmptyArray() throws Exception {
        when(taskService.getMyTasks("john@example.com")).thenReturn(List.of());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ══════════════════ GET /api/tasks/{id} ══════════════════

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("GET /tasks/{id}: éxito - devuelve la tarea")
    void getById_authenticated_returnsTask() throws Exception {
        when(taskService.getById(1L, "john@example.com")).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Tarea de prueba"));
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("GET /tasks/{id}: tarea ajena - servicio lanza excepción - devuelve 400")
    void getById_notOwned_returns400() throws Exception {
        when(taskService.getById(99L, "john@example.com"))
                .thenThrow(new IllegalArgumentException("Tarea no encontrada"));

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Tarea no encontrada"));
    }

    // ══════════════════ PUT /api/tasks/{id} ══════════════════

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("PUT /tasks/{id}: éxito - actualiza y devuelve la tarea")
    void updateTask_authenticated_returns200() throws Exception {
        TaskRequest req = buildTaskRequest("Título actualizado", TaskStatus.COMPLETED, Priority.LOW);

        TaskResponse updated = TaskResponse.builder()
                .id(1L).title("Título actualizado").status(TaskStatus.COMPLETED)
                .priority(Priority.LOW).ownerUsername("johndoe")
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(taskService.update(eq(1L), any(TaskRequest.class), eq("john@example.com")))
                .thenReturn(updated);

        mockMvc.perform(put("/api/tasks/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Título actualizado"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("PUT /tasks/{id}: título vacío - devuelve 400")
    void updateTask_emptyTitle_returns400() throws Exception {
        TaskRequest req = buildTaskRequest("", TaskStatus.PENDING, Priority.MEDIUM);

        mockMvc.perform(put("/api/tasks/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ══════════════════ DELETE /api/tasks/{id} ══════════════════

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("DELETE /tasks/{id}: éxito - devuelve 204 No Content")
    void deleteTask_authenticated_returns204() throws Exception {
        doNothing().when(taskService).delete(1L, "john@example.com");

        mockMvc.perform(delete("/api/tasks/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(taskService).delete(1L, "john@example.com");
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("DELETE /tasks/{id}: tarea no encontrada - devuelve 400")
    void deleteTask_notFound_returns400() throws Exception {
        doThrow(new IllegalArgumentException("Tarea no encontrada"))
                .when(taskService).delete(999L, "john@example.com");

        mockMvc.perform(delete("/api/tasks/999").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Tarea no encontrada"));
    }

    @Test
    @DisplayName("DELETE /tasks/{id}: sin autenticación - devuelve 401 o 403")
    void deleteTask_unauthenticated_returns401or403() throws Exception {
        mockMvc.perform(delete("/api/tasks/1").with(csrf()))
                .andExpect(status().is4xxClientError());
    }

    // ══════════════════ Helper ══════════════════

    private TaskRequest buildTaskRequest(String title, TaskStatus status, Priority priority) {
        TaskRequest r = new TaskRequest();
        r.setTitle(title);
        r.setStatus(status);
        r.setPriority(priority);
        return r;
    }
}
