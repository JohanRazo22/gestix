package com.gestix.repository;

import com.gestix.entity.Task;
import com.gestix.enums.Priority;
import com.gestix.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwnerId(Long ownerId);
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByOwnerIdAndStatus(Long ownerId, TaskStatus status);
    List<Task> findByOwnerIdAndPriority(Long ownerId, Priority priority);
    List<Task> findByOwnerIdAndCategory(Long ownerId, String category);
}
