package com.teamtaskmanager.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record VisibleProjectsRequest(@NotEmpty List<String> projectNames) {}
