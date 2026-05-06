package com.teamtaskmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductivityFlagResponse(
    Long sessionId,
    LocalDate workDate,
    LocalDateTime punchedInAt,
    LocalDateTime punchedOutAt,
    UserResponse tasker,
    long taskingSeconds,
    long expectedTaskingSeconds,
    long extraSeconds,
    int completedTasks,
    String reason) {}
