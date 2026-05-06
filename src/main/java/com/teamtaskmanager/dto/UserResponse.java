package com.teamtaskmanager.dto;

import com.teamtaskmanager.model.Role;

public record UserResponse(Long id, String name, String email, Role role) {}
