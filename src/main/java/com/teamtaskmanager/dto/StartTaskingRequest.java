package com.teamtaskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StartTaskingRequest(
    @NotBlank
    @Pattern(regexp = "\\d+", message = "Task ID must contain only numbers")
    String externalTaskId,
    @NotBlank String projectName) {}
