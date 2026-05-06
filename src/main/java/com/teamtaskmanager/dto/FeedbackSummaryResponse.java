package com.teamtaskmanager.dto;

import java.util.List;

public record FeedbackSummaryResponse(
    double averageRating,
    int feedbackCount,
    List<FeedbackResponse> feedback) {}
