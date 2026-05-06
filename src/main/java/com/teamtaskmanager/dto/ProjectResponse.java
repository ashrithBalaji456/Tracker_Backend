package com.teamtaskmanager.dto;

import java.util.List;

public record ProjectResponse(
    Long id,
    String name,
    String description,
    UserResponse createdBy,
    List<UserResponse> members,
    int taskCount) {}
