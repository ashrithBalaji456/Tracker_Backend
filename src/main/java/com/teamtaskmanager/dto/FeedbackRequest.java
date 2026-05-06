package com.teamtaskmanager.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record FeedbackRequest(
    @NotBlank String message,
    @Min(1) @Max(3) int rating,
    LocalDate workDate) {}
