package com.teamtaskmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FeedbackResponse(
    Long id,
    String message,
    int rating,
    LocalDate workDate,
    LocalDateTime createdAt,
    UserResponse sentBy) {}
