package com.teamtaskmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TaskerReportResponse(
    UserResponse tasker,
    LocalDate workDate,
    LocalDateTime punchedInAt,
    LocalDateTime punchedOutAt,
    long loginSeconds,
    long taskingSeconds,
    long expectedTaskingSeconds,
    int completedTasks,
    String attendanceStatus,
    boolean flagged,
    String flagReason,
    List<String> flagReasons,
    double averageRating) {}
