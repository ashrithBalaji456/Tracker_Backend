package com.teamtaskmanager.service;

import com.teamtaskmanager.exception.ApiException;
import com.teamtaskmanager.model.User;
import com.teamtaskmanager.repository.UserRepository;
import com.teamtaskmanager.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
  private final UserRepository userRepository;

  public CurrentUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User user() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserPrincipal userPrincipal) {
      return userRepository.findById(userPrincipal.getId())
          .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User no longer exists"));
    }
    throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication required");
  }
}
