package com.teamtaskmanager.controller;

import com.teamtaskmanager.dto.ProjectRequest;
import com.teamtaskmanager.dto.ProjectResponse;
import com.teamtaskmanager.service.ProjectService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProjectResponse create(@Valid @RequestBody ProjectRequest request) {
    return projectService.create(request);
  }

  @GetMapping
  public List<ProjectResponse> list() {
    return projectService.list();
  }

  @GetMapping("/{id}")
  public ProjectResponse get(@PathVariable Long id) {
    return projectService.get(id);
  }

  @PutMapping("/{id}")
  public ProjectResponse update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
    return projectService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    projectService.delete(id);
  }

  @PostMapping("/{projectId}/members/{userId}")
  public ProjectResponse addMember(@PathVariable Long projectId, @PathVariable Long userId) {
    return projectService.addMember(projectId, userId);
  }

  @DeleteMapping("/{projectId}/members/{userId}")
  public ProjectResponse removeMember(@PathVariable Long projectId, @PathVariable Long userId) {
    return projectService.removeMember(projectId, userId);
  }
}
