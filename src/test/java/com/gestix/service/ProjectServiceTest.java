package com.gestix.service;

import com.gestix.dto.ProjectRequest;
import com.gestix.dto.ProjectResponse;
import com.gestix.entity.Project;
import com.gestix.entity.Task;
import com.gestix.entity.User;
import com.gestix.repository.ProjectRepository;
import com.gestix.repository.TaskRepository;
import com.gestix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectService - Pruebas unitarias")
class ProjectServiceTest {

    @Mock private ProjectRepository projectRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private User owner;
    private Project sampleProject;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .password("HASHED")
                .build();

        sampleProject = Project.builder()
                .id(5L)
                .name("Marketing")
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("getMyProjects: devuelve proyectos del usuario")
    void getMyProjects_success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(projectRepository.findByOwnerIdOrderByNameAsc(1L)).thenReturn(List.of(sampleProject));

        List<ProjectResponse> result = projectService.getMyProjects("john@example.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Marketing");
    }

    @Test
    @DisplayName("create: guarda proyecto nuevo")
    void create_success() {
        ProjectRequest req = new ProjectRequest();
        req.setName("Marketing");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(projectRepository.existsByOwnerIdAndName(1L, "Marketing")).thenReturn(false);
        when(projectRepository.save(any(Project.class))).thenReturn(sampleProject);

        ProjectResponse response = projectService.create(req, "john@example.com");

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getName()).isEqualTo("Marketing");
    }

    @Test
    @DisplayName("create: nombre duplicado lanza excepción")
    void create_duplicateName_throws() {
        ProjectRequest req = new ProjectRequest();
        req.setName("Marketing");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(projectRepository.existsByOwnerIdAndName(1L, "Marketing")).thenReturn(true);

        assertThatThrownBy(() -> projectService.create(req, "john@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ya existe un proyecto con ese nombre");
    }

    @Test
    @DisplayName("delete: elimina tareas del proyecto y el proyecto")
    void delete_success() {
        Task task = Task.builder().id(10L).category("Marketing").owner(owner).build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(projectRepository.findByIdAndOwnerId(5L, 1L)).thenReturn(Optional.of(sampleProject));
        when(taskRepository.findByOwnerIdAndCategory(1L, "Marketing")).thenReturn(List.of(task));

        projectService.delete(5L, "john@example.com");

        verify(taskRepository).deleteAll(List.of(task));
        verify(projectRepository).delete(sampleProject);
    }

    @Test
    @DisplayName("delete: proyecto ajeno lanza excepción")
    void delete_notOwned_throws() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(projectRepository.findByIdAndOwnerId(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.delete(99L, "john@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Proyecto no encontrado");
    }
}
