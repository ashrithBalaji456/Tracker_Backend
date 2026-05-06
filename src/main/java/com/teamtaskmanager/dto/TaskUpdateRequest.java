package com.teamtaskmanager.dto;

import com.teamtaskmanager.model.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record TaskUpdateRequest(
    @Size(max = 140) String title,
    @Size(max = 1000) String description,
    TaskStatus status,
    @FutureOrPresent LocalDate dueDate,
    Long assignedToId,
    Long projectId) {}
