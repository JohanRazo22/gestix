package com.gestix.repository;

import com.gestix.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerIdOrderByNameAsc(Long ownerId);

    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);

    Optional<Project> findByOwnerIdAndName(Long ownerId, String name);

    boolean existsByOwnerIdAndName(Long ownerId, String name);
}
