package com.gestix.service;

import com.gestix.dto.ProjectRequest;
import com.gestix.dto.ProjectResponse;
import com.gestix.entity.Project;
import com.gestix.entity.Task;
import com.gestix.entity.User;
import com.gestix.repository.ProjectRepository;
import com.gestix.repository.TaskRepository;
import com.gestix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<ProjectResponse> getMyProjects(String email) {
        User owner = getUserByEmail(email);
        return projectRepository.findByOwnerIdOrderByNameAsc(owner.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProjectResponse create(ProjectRequest request, String email) {
        User owner = getUserByEmail(email);
        String name = request.getName().trim();

        if (projectRepository.existsByOwnerIdAndName(owner.getId(), name)) {
            throw new IllegalArgumentException("Ya existe un proyecto con ese nombre");
        }

        Project project = Project.builder()
                .name(name)
                .owner(owner)
                .build();

        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public void delete(Long id, String email) {
        User owner = getUserByEmail(email);
        Project project = projectRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        List<Task> tasks = taskRepository.findByOwnerIdAndCategory(owner.getId(), project.getName());
        taskRepository.deleteAll(tasks);
        projectRepository.delete(project);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    private ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
