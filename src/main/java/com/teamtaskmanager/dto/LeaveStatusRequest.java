package com.teamtaskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public record LeaveStatusRequest(@NotBlank String status) {}
