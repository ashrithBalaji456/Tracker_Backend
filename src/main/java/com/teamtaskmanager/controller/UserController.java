package com.teamtaskmanager.controller;

import com.teamtaskmanager.dto.UserResponse;
import com.teamtaskmanager.exception.ApiException;
import com.teamtaskmanager.model.Role;
import com.teamtaskmanager.repository.UserRepository;
import com.teamtaskmanager.service.CurrentUserService;
import com.teamtaskmanager.service.Mapper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final CurrentUserService currentUserService;
  private final UserRepository userRepository;

  public UserController(CurrentUserService currentUserService, UserRepository userRepository) {
    this.currentUserService = currentUserService;
    this.userRepository = userRepository;
  }

  @GetMapping("/me")
  public UserResponse me() {
    return Mapper.user(currentUserService.user());
  }

  @GetMapping
  public List<UserResponse> users() {
    if (currentUserService.user().getRole() != Role.ADMIN) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Admin role required");
    }
    return userRepository.findAll().stream()
        .map(Mapper::user)
        .toList();
  }
}
