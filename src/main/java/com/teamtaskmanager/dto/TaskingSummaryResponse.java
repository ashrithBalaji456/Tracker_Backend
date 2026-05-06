package com.teamtaskmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TaskingSummaryResponse(
    LocalDate workDate,
    boolean punchedIn,
    boolean canPunchOut,
    LocalDateTime punchedInAt,
    LocalDateTime punchedOutAt,
    long loginSeconds,
    long taskingSeconds,
    long expectedTaskingSeconds,
    boolean productivityFlagged,
    String productivityFlagReason,
    String attendanceStatus,
    long requiredTaskingSeconds,
    long requiredLoginSeconds,
    List<String> visibleProjectNames,
    int completedTasksToday,
    TaskLogResponse activeTask,
    List<TaskLogResponse> completedTasks) {}
