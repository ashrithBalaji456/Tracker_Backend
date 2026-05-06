package com.teamtaskmanager.dto;

import java.util.List;

public record AdminTaskerHistoryResponse(
    UserResponse tasker,
    long totalLoginSeconds,
    long totalTaskingSeconds,
    int totalTasks,
    double averageRating,
    List<FeedbackResponse> feedback,
    List<TaskingSummaryResponse> days) {}
