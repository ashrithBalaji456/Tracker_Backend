package com.teamtaskmanager.dto;

import com.teamtaskmanager.model.TaskStatus;
import java.time.LocalDate;

public record TaskResponse(
    Long id,
    String title,
    String description,
    TaskStatus status,
    LocalDate dueDate,
    boolean overdue,
    UserResponse assignedTo,
    Long projectId,
    String projectName) {}
