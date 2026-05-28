package com.gestix.service;

import com.gestix.dto.TaskRequest;
import com.gestix.dto.TaskResponse;
import com.gestix.entity.Task;
import com.gestix.entity.User;
import com.gestix.repository.TaskRepository;
import com.gestix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse create(TaskRequest request, String email) {
        User owner = getUserByEmail(email);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .category(request.getCategory())
                .dueDate(request.getDueDate())
                .owner(owner)
                .assignedTo(resolveAssignedTo(request.getAssignedToId()))
                .build();

        return toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getMyTasks(String email) {
        User owner = getUserByEmail(email);
        return taskRepository.findByOwnerId(owner.getId())
                .stream().map(this::toResponse).toList();
    }

    public TaskResponse getById(Long id, String email) {
        Task task = findTaskOwned(id, email);
        return toResponse(task);
    }

    public TaskResponse update(Long id, TaskRequest request, String email) {
        Task task = findTaskOwned(id, email);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        task.setCategory(request.getCategory());
        task.setDueDate(request.getDueDate());
        task.setAssignedTo(resolveAssignedTo(request.getAssignedToId()));

        return toResponse(taskRepository.save(task));
    }

    public void delete(Long id, String email) {
        Task task = findTaskOwned(id, email);
        taskRepository.delete(task);
    }

    private Task findTaskOwned(Long id, String email) {
        User owner = getUserByEmail(email);
        return taskRepository.findById(id)
                .filter(t -> t.getOwner().getId().equals(owner.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    private User resolveAssignedTo(Long assignedToId) {
        if (assignedToId == null) return null;
        return userRepository.findById(assignedToId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario asignado no encontrado"));
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .category(task.getCategory())
                .dueDate(task.getDueDate())
                .ownerUsername(task.getOwner().getUsername())
                .assignedToUsername(task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
