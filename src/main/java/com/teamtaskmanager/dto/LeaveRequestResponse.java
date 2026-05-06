package com.teamtaskmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveRequestResponse(
    Long id,
    UserResponse tasker,
    LocalDate startDate,
    LocalDate endDate,
    String reason,
    String status,
    LocalDateTime requestedAt) {}
