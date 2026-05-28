package com.gestix.dto;

import com.gestix.enums.Priority;
import com.gestix.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank(message = "El titulo es obligatorio")
    private String title;

    private String description;

    private TaskStatus status;

    private Priority priority;

    private String category;

    private LocalDate dueDate;

    private Long assignedToId;
}
