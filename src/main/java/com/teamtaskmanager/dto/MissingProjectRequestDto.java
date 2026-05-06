package com.teamtaskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MissingProjectRequestDto(
    @NotBlank @Size(max = 220) String projectName,
    @Size(max = 1000) String note) {}
