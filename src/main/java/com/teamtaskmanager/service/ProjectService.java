package com.teamtaskmanager.service;

import com.teamtaskmanager.dto.ProjectRequest;
import com.teamtaskmanager.dto.ProjectResponse;
import com.teamtaskmanager.exception.ApiException;
import com.teamtaskmanager.model.Project;
import com.teamtaskmanager.model.Role;
import com.teamtaskmanager.model.User;
import com.teamtaskmanager.repository.ProjectRepository;
import com.teamtaskmanager.repository.UserRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;
  private final CurrentUserService currentUserService;

  public ProjectService(
      ProjectRepository projectRepository,
      UserRepository userRepository,
      CurrentUserService currentUserService) {
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
    this.currentUserService = currentUserService;
  }

  @Transactional
  public ProjectResponse create(ProjectRequest request) {
    User user = requireAdmin();
    Project project = new Project();
    project.setName(request.name().trim());
    project.setDescription(request.description());
    project.setCreatedBy(user);
    project.getMembers().add(user);
    return Mapper.project(projectRepository.save(project));
  }

  @Transactional(readOnly = true)
  public List<ProjectResponse> list() {
    User user = currentUserService.user();
    List<Project> projects = user.getRole() == Role.ADMIN
        ? projectRepository.findAll()
        : projectRepository.findDistinctByMembersIdOrCreatedById(user.getId(), user.getId());
    return projects.stream().map(Mapper::project).toList();
  }

  @Transactional(readOnly = true)
  public ProjectResponse get(Long id) {
    return Mapper.project(requireProjectAccess(id));
  }

  @Transactional
  public ProjectResponse update(Long id, ProjectRequest request) {
    requireAdmin();
    Project project = requireProject(id);
    project.setName(request.name().trim());
    project.setDescription(request.description());
    return Mapper.project(project);
  }

  @Transactional
  public void delete(Long id) {
    requireAdmin();
    projectRepository.delete(requireProject(id));
  }

  @Transactional
  public ProjectResponse addMember(Long projectId, Long userId) {
    requireAdmin();
    Project project = requireProject(projectId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    project.getMembers().add(user);
    return Mapper.project(project);
  }

  @Transactional
  public ProjectResponse removeMember(Long projectId, Long userId) {
    requireAdmin();
    Project project = requireProject(projectId);
    project.getMembers().removeIf(user -> user.getId().equals(userId));
    return Mapper.project(project);
  }

  Project requireProjectAccess(Long id) {
    User user = currentUserService.user();
    Project project = requireProject(id);
    boolean member = project.getMembers().stream().anyMatch(memberUser -> memberUser.getId().equals(user.getId()));
    if (user.getRole() == Role.ADMIN || project.getCreatedBy().getId().equals(user.getId()) || member) {
      return project;
    }
    throw new ApiException(HttpStatus.FORBIDDEN, "Project is not available to this user");
  }

  private User requireAdmin() {
    User user = currentUserService.user();
    if (user.getRole() != Role.ADMIN) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Admin role required");
    }
    return user;
  }

  private Project requireProject(Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Project not found"));
  }
}
