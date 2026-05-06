package com.teamtaskmanager.dto;

import com.teamtaskmanager.model.TaskStatus;
import java.util.List;
import java.util.Map;

public record DashboardResponse(
    long totalTasks,
    long completedTasks,
    long overdueTasks,
    Map<TaskStatus, Long> tasksByStatus,
    List<TaskResponse> overdue,
    List<TaskResponse> upcoming) {}
