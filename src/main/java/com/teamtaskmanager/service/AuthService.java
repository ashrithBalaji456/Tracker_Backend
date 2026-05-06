package com.teamtaskmanager.service;

import com.teamtaskmanager.dto.AuthRequest;
import com.teamtaskmanager.dto.AuthResponse;
import com.teamtaskmanager.dto.SignupRequest;
import com.teamtaskmanager.exception.ApiException;
import com.teamtaskmanager.model.Role;
import com.teamtaskmanager.model.User;
import com.teamtaskmanager.repository.UserRepository;
import com.teamtaskmanager.security.JwtService;
import com.teamtaskmanager.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  public AuthResponse signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
    }
    User user = new User();
    user.setName(request.name().trim());
    user.setEmail(request.email().trim().toLowerCase());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setRole(request.role() == null ? Role.MEMBER : request.role());
    User saved = userRepository.save(user);
    String token = jwtService.generateToken(new UserPrincipal(saved));
    return new AuthResponse(token, Mapper.user(saved));
  }

  public AuthResponse login(AuthRequest request) {
    var auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
    return new AuthResponse(jwtService.generateToken(principal), Mapper.user(principal.getUser()));
  }
}
