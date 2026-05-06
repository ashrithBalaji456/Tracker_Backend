package com.teamtaskmanager.dto;

import java.time.LocalDateTime;

public record TaskLogResponse(
    Long id,
    String externalTaskId,
    String projectName,
    String domain,
    double minutesPerTask,
    LocalDateTime startedAt,
    LocalDateTime submittedAt,
    long expectedDurationSeconds,
    long durationSeconds,
    long exceededSeconds,
    double exceededPercentage,
    boolean noJustification,
    boolean submitted) {}
