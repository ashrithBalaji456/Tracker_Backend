package com.teamtaskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(length = 1000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TaskStatus status = TaskStatus.TODO;

  @Column(nullable = false)
  private LocalDate dueDate;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User assignedTo;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Project project;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public TaskStatus getStatus() { return status; }
  public void setStatus(TaskStatus status) { this.status = status; }
  public LocalDate getDueDate() { return dueDate; }
  public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
  public User getAssignedTo() { return assignedTo; }
  public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }
  public Project getProject() { return project; }
  public void setProject(Project project) { this.project = project; }
}
