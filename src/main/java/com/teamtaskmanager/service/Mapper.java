package com.teamtaskmanager.service;

import com.teamtaskmanager.dto.ProjectResponse;
import com.teamtaskmanager.dto.TaskResponse;
import com.teamtaskmanager.dto.UserResponse;
import com.teamtaskmanager.model.Project;
import com.teamtaskmanager.model.Task;
import com.teamtaskmanager.model.TaskStatus;
import com.teamtaskmanager.model.User;
import java.time.LocalDate;

public final class Mapper {
  private Mapper() {}

  public static UserResponse user(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
  }

  public static ProjectResponse project(Project project) {
    return new ProjectResponse(
        project.getId(),
        project.getName(),
        project.getDescription(),
        user(project.getCreatedBy()),
        project.getMembers().stream().map(Mapper::user).toList(),
        project.getTasks().size());
  }

  public static TaskResponse task(Task task) {
    boolean overdue = task.getDueDate().isBefore(LocalDate.now())
        && task.getStatus() != TaskStatus.DONE;
    return new TaskResponse(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getStatus(),
        task.getDueDate(),
        overdue,
        user(task.getAssignedTo()),
        task.getProject().getId(),
        task.getProject().getName());
  }
}
