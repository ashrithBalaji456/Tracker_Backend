package com.teamtaskmanager.dto;

import com.teamtaskmanager.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank @Size(min = 2, max = 80) String name,
    @Email @NotBlank String email,
    @NotBlank @Size(min = 8, max = 72) String password,
    Role role) {}
