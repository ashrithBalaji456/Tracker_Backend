package com.teamtaskmanager.service;

import com.teamtaskmanager.dto.DashboardResponse;
import com.teamtaskmanager.model.Task;
import com.teamtaskmanager.model.TaskStatus;
import com.teamtaskmanager.model.User;
import com.teamtaskmanager.repository.TaskRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
  private final TaskRepository taskRepository;
  private final CurrentUserService currentUserService;

  public DashboardService(TaskRepository taskRepository, CurrentUserService currentUserService) {
    this.taskRepository = taskRepository;
    this.currentUserService = currentUserService;
  }

  @Transactional(readOnly = true)
  public DashboardResponse dashboard() {
    User user = currentUserService.user();
    var tasks = taskRepository.findByAssignedToId(user.getId());
    LocalDate today = LocalDate.now();
    Map<TaskStatus, Long> byStatus = Arrays.stream(TaskStatus.values())
        .collect(Collectors.toMap(status -> status, status -> tasks.stream().filter(t -> t.getStatus() == status).count()));
    var overdue = tasks.stream()
        .filter(task -> task.getDueDate().isBefore(today) && task.getStatus() != TaskStatus.DONE)
        .sorted(Comparator.comparing(Task::getDueDate))
        .map(Mapper::task)
        .toList();
    var upcoming = tasks.stream()
        .filter(task -> !task.getDueDate().isBefore(today) && task.getStatus() != TaskStatus.DONE)
        .sorted(Comparator.comparing(Task::getDueDate))
        .limit(6)
        .map(Mapper::task)
        .toList();
    return new DashboardResponse(
        tasks.size(),
        byStatus.get(TaskStatus.DONE),
        overdue.size(),
        byStatus,
        overdue,
        upcoming);
  }
}
