package com.teamtaskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "project_name"}))
public class VisibleProjectSelection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User user;

  @Column(name = "project_name", nullable = false, length = 220)
  private String projectName;

  @Column(nullable = false)
  private LocalDateTime selectedAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public String getProjectName() { return projectName; }
  public void setProjectName(String projectName) { this.projectName = projectName; }
  public LocalDateTime getSelectedAt() { return selectedAt; }
  public void setSelectedAt(LocalDateTime selectedAt) { this.selectedAt = selectedAt; }
}
