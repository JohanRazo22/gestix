package com.gestix.service;

import com.gestix.dto.TaskRequest;
import com.gestix.dto.TaskResponse;
import com.gestix.entity.Task;
import com.gestix.entity.User;
import com.gestix.enums.Priority;
import com.gestix.enums.TaskStatus;
import com.gestix.repository.TaskRepository;
import com.gestix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService - Pruebas unitarias")
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User owner;
    private Task sampleTask;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .password("HASHED")
                .build();

        sampleTask = Task.builder()
                .id(10L)
                .title("Tarea de prueba")
                .description("Descripcion de prueba")
                .status(TaskStatus.PENDING)
                .priority(Priority.HIGH)
                .category("Trabajo")
                .dueDate(LocalDate.of(2026, 12, 31))
                .owner(owner)
                .assignedTo(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ══════════════════ CREATE ══════════════════

    @Test
    @DisplayName("create: éxito - guarda la tarea y devuelve TaskResponse")
    void create_success_returnsTaskResponse() {
        TaskRequest req = buildRequest("Tarea de prueba", "Descripcion", TaskStatus.PENDING, Priority.HIGH, "Trabajo", null, null);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        TaskResponse response = taskService.create(req, "john@example.com");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("Tarea de prueba");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(response.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(response.getOwnerUsername()).isEqualTo("johndoe");
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("create: usuario no existe - lanza IllegalArgumentException")
    void create_userNotFound_throwsException() {
        TaskRequest req = buildRequest("Tarea", null, null, null, null, null, null);
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.create(req, "ghost@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no encontrado");

        verify(taskRepository, never()).save(any());
    }

    // ══════════════════ GET MY TASKS ══════════════════

    @Test
    @DisplayName("getMyTasks: devuelve lista de tareas del usuario")
    void getMyTasks_returnsTaskList() {
        Task task2 = Task.builder()
                .id(11L).title("Segunda tarea").status(TaskStatus.IN_PROGRESS)
                .priority(Priority.LOW).owner(owner)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.findByOwnerId(1L)).thenReturn(List.of(sampleTask, task2));

        List<TaskResponse> tasks = taskService.getMyTasks("john@example.com");

        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Tarea de prueba");
        assertThat(tasks.get(1).getTitle()).isEqualTo("Segunda tarea");
    }

    @Test
    @DisplayName("getMyTasks: sin tareas - devuelve lista vacía")
    void getMyTasks_empty_returnsEmptyList() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.findByOwnerId(1L)).thenReturn(List.of());

        List<TaskResponse> tasks = taskService.getMyTasks("john@example.com");

        assertThat(tasks).isEmpty();
    }

    // ══════════════════ GET BY ID ══════════════════

    @Test
    @DisplayName("getById: éxito - devuelve la tarea del dueño")
    void getById_success_returnsTask() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(sampleTask));

        TaskResponse response = taskService.getById(10L, "john@example.com");

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("Tarea de prueba");
    }

    @Test
    @DisplayName("getById: tarea de otro usuario - lanza IllegalArgumentException")
    void getById_notOwned_throwsException() {
        User otherUser = User.builder().id(99L).username("other").email("other@example.com").build();
        Task otherTask = Task.builder()
                .id(10L).title("Tarea ajena").owner(otherUser)
                .status(TaskStatus.PENDING).priority(Priority.LOW)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(otherTask));

        assertThatThrownBy(() -> taskService.getById(10L, "john@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no encontrada");
    }

    @Test
    @DisplayName("getById: tarea no existe - lanza IllegalArgumentException")
    void getById_notFound_throwsException() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getById(999L, "john@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ══════════════════ UPDATE ══════════════════

    @Test
    @DisplayName("update: éxito - actualiza y devuelve TaskResponse con nuevos valores")
    void update_success_returnsUpdatedTask() {
        TaskRequest updateReq = buildRequest("Título actualizado", "Desc nueva",
                TaskStatus.COMPLETED, Priority.LOW, "Personal", null, null);

        Task updatedTask = Task.builder()
                .id(10L).title("Título actualizado").description("Desc nueva")
                .status(TaskStatus.COMPLETED).priority(Priority.LOW)
                .category("Personal").owner(owner)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskResponse response = taskService.update(10L, updateReq, "john@example.com");

        assertThat(response.getTitle()).isEqualTo("Título actualizado");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(response.getPriority()).isEqualTo(Priority.LOW);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("update: tarea ajena - lanza IllegalArgumentException")
    void update_notOwned_throwsException() {
        User other = User.builder().id(2L).username("other").email("other@example.com").build();
        Task foreignTask = Task.builder()
                .id(10L).title("Ajena").owner(other)
                .status(TaskStatus.PENDING).priority(Priority.MEDIUM)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        TaskRequest req = buildRequest("Nuevo título", null, null, null, null, null, null);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(foreignTask));

        assertThatThrownBy(() -> taskService.update(10L, req, "john@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(taskRepository, never()).save(any());
    }

    // ══════════════════ DELETE ══════════════════

    @Test
    @DisplayName("delete: éxito - elimina la tarea del repositorio")
    void delete_success_deletesTask() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(sampleTask));

        assertThatCode(() -> taskService.delete(10L, "john@example.com"))
                .doesNotThrowAnyException();

        verify(taskRepository).delete(sampleTask);
    }

    @Test
    @DisplayName("delete: tarea ajena - lanza IllegalArgumentException, no borra nada")
    void delete_notOwned_throwsException() {
        User other = User.builder().id(2L).username("other").email("other@example.com").build();
        Task foreignTask = Task.builder()
                .id(10L).title("Ajena").owner(other)
                .status(TaskStatus.PENDING).priority(Priority.MEDIUM)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(foreignTask));

        assertThatThrownBy(() -> taskService.delete(10L, "john@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(taskRepository, never()).delete(any());
    }

    // ══════════════════ ASSIGNED TO ══════════════════

    @Test
    @DisplayName("create: con assignedToId válido - asigna al usuario correcto")
    void create_withAssignedTo_setsAssignedUser() {
        User assigned = User.builder().id(2L).username("jane").email("jane@example.com").build();
        TaskRequest req = buildRequest("Tarea asignada", null, TaskStatus.PENDING, Priority.MEDIUM, null, null, 2L);

        Task savedTask = Task.builder()
                .id(20L).title("Tarea asignada").status(TaskStatus.PENDING).priority(Priority.MEDIUM)
                .owner(owner).assignedTo(assigned)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assigned));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = taskService.create(req, "john@example.com");

        assertThat(response.getAssignedToUsername()).isEqualTo("jane");
    }

    @Test
    @DisplayName("create: assignedToId no existe - lanza IllegalArgumentException")
    void create_assignedToNotFound_throwsException() {
        TaskRequest req = buildRequest("Tarea", null, null, null, null, null, 999L);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(owner));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.create(req, "john@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("asignado");
    }

    // ══════════════════ Helper ══════════════════

    private TaskRequest buildRequest(String title, String desc, TaskStatus status,
                                     Priority priority, String category,
                                     LocalDate dueDate, Long assignedToId) {
        TaskRequest r = new TaskRequest();
        r.setTitle(title);
        r.setDescription(desc);
        r.setStatus(status);
        r.setPriority(priority);
        r.setCategory(category);
        r.setDueDate(dueDate);
        r.setAssignedToId(assignedToId);
        return r;
    }
}
