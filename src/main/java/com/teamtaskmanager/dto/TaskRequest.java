package com.teamtaskmanager.dto;

import com.teamtaskmanager.model.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record TaskRequest(
    @NotBlank @Size(max = 140) String title,
    @Size(max = 1000) String description,
    TaskStatus status,
    @NotNull @FutureOrPresent LocalDate dueDate,
    @NotNull Long assignedToId,
    @NotNull Long projectId) {}
