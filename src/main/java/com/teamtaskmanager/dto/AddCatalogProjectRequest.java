package com.teamtaskmanager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record AddCatalogProjectRequest(
    @NotBlank String name,
    @DecimalMin(value = "0.1", message = "AHT must be greater than 0") double minutesPerTask,
    @NotBlank String domain,
    boolean justificationExpected) {}
