package com.teamtaskmanager.dto;

import java.time.LocalDateTime;

public record MissingProjectResponse(
    Long id,
    String projectName,
    String note,
    LocalDateTime requestedAt,
    UserResponse requestedBy,
    boolean resolved) {}
