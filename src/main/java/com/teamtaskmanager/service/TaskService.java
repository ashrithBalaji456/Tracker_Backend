package com.teamtaskmanager.service;

import com.teamtaskmanager.dto.TaskRequest;
import com.teamtaskmanager.dto.TaskResponse;
import com.teamtaskmanager.dto.TaskUpdateRequest;
import com.teamtaskmanager.exception.ApiException;
import com.teamtaskmanager.model.Project;
import com.teamtaskmanager.model.Role;
import com.teamtaskmanager.model.Task;
import com.teamtaskmanager.model.TaskStatus;
import com.teamtaskmanager.model.User;
import com.teamtaskmanager.repository.TaskRepository;
import com.teamtaskmanager.repository.UserRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final ProjectService projectService;
  private final CurrentUserService currentUserService;

  public TaskService(
      TaskRepository taskRepository,
      UserRepository userRepository,
      ProjectService projectService,
      CurrentUserService currentUserService) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
    this.projectService = projectService;
    this.currentUserService = currentUserService;
  }

  @Transactional
  public TaskResponse create(TaskRequest request) {
    requireAdmin();
    Project project = projectService.requireProjectAccess(request.projectId());
    User assigned = requireProjectMember(project, request.assignedToId());
    Task task = new Task();
    task.setTitle(request.title().trim());
    task.setDescription(request.description());
    task.setStatus(request.status() == null ? TaskStatus.TODO : request.status());
    task.setDueDate(request.dueDate());
    task.setAssignedTo(assigned);
    task.setProject(project);
    return Mapper.task(taskRepository.save(task));
  }

  @Transactional(readOnly = true)
  public List<TaskResponse> list() {
    User user = currentUserService.user();
    List<Task> tasks = user.getRole() == Role.ADMIN
        ? taskRepository.findAll()
        : taskRepository.findByAssignedToId(user.getId());
    return tasks.stream().map(Mapper::task).toList();
  }

  @Transactional
  public TaskResponse update(Long id, TaskUpdateRequest request) {
    Task task = requireTaskAccess(id);
    User actor = currentUserService.user();
    if (actor.getRole() == Role.MEMBER) {
      if (request.status() == null) {
        throw new ApiException(HttpStatus.FORBIDDEN, "Members may only update task status");
      }
      task.setStatus(request.status());
      return Mapper.task(task);
    }

    if (request.title() != null) task.setTitle(request.title().trim());
    if (request.description() != null) task.setDescription(request.description());
    if (request.status() != null) task.setStatus(request.status());
    if (request.dueDate() != null) task.setDueDate(request.dueDate());
    if (request.projectId() != null) task.setProject(projectService.requireProjectAccess(request.projectId()));
    if (request.assignedToId() != null) task.setAssignedTo(requireProjectMember(task.getProject(), request.assignedToId()));
    return Mapper.task(task);
  }

  @Transactional
  public void delete(Long id) {
    requireAdmin();
    taskRepository.delete(taskRepository.findById(id)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Task not found")));
  }

  Task requireTaskAccess(Long id) {
    User user = currentUserService.user();
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Task not found"));
    boolean assigned = task.getAssignedTo().getId().equals(user.getId());
    if (user.getRole() == Role.ADMIN || assigned) {
      return task;
    }
    throw new ApiException(HttpStatus.FORBIDDEN, "Task is not available to this user");
  }

  private User requireAdmin() {
    User user = currentUserService.user();
    if (user.getRole() != Role.ADMIN) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Admin role required");
    }
    return user;
  }

  private User requireProjectMember(Project project, Long userId) {
    return project.getMembers().stream()
        .filter(user -> user.getId().equals(userId))
        .findFirst()
        .orElseGet(() -> userRepository.findById(userId)
            .filter(user -> project.getCreatedBy().getId().equals(user.getId()))
            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Assignee must be a project member")));
  }
}
