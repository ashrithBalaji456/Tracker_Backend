package com.teamtaskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MissingProjectRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User requestedBy;

  @Column(nullable = false, length = 220)
  private String projectName;

  @Column(length = 1000)
  private String note;

  @Column(nullable = false)
  private LocalDateTime requestedAt;

  @Column(nullable = false)
  private boolean resolved = false;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public User getRequestedBy() { return requestedBy; }
  public void setRequestedBy(User requestedBy) { this.requestedBy = requestedBy; }
  public String getProjectName() { return projectName; }
  public void setProjectName(String projectName) { this.projectName = projectName; }
  public String getNote() { return note; }
  public void setNote(String note) { this.note = note; }
  public LocalDateTime getRequestedAt() { return requestedAt; }
  public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
  public boolean isResolved() { return resolved; }
  public void setResolved(boolean resolved) { this.resolved = resolved; }
}
