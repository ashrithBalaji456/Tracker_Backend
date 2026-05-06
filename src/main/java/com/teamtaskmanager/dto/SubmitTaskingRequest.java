package com.teamtaskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubmitTaskingRequest(
    @NotBlank @Size(max = 4000) String promptText,
    @Size(max = 4000) String justification,
    boolean noJustification) {}
