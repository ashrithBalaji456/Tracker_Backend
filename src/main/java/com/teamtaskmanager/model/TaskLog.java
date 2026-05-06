package com.teamtaskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class TaskLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User user;

  @Column(nullable = false)
  private LocalDate workDate;

  @Column(nullable = false)
  private String externalTaskId;

  @Column(nullable = false, length = 180)
  private String projectName;

  @Column(nullable = false)
  private String domain;

  @Column(nullable = false)
  private double minutesPerTask;

  @Column(nullable = false)
  private LocalDateTime startedAt;

  private LocalDateTime submittedAt;

  @Column(nullable = false)
  private long durationSeconds = 0;

  @Column(length = 4000)
  private String promptText;

  @Column(length = 4000)
  private String justification;

  @Column(nullable = false)
  private boolean noJustification;

  @Column(nullable = false)
  private boolean submitted = false;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public LocalDate getWorkDate() { return workDate; }
  public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }
  public String getExternalTaskId() { return externalTaskId; }
  public void setExternalTaskId(String externalTaskId) { this.externalTaskId = externalTaskId; }
  public String getProjectName() { return projectName; }
  public void setProjectName(String projectName) { this.projectName = projectName; }
  public String getDomain() { return domain; }
  public void setDomain(String domain) { this.domain = domain; }
  public double getMinutesPerTask() { return minutesPerTask; }
  public void setMinutesPerTask(double minutesPerTask) { this.minutesPerTask = minutesPerTask; }
  public LocalDateTime getStartedAt() { return startedAt; }
  public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
  public LocalDateTime getSubmittedAt() { return submittedAt; }
  public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
  public long getDurationSeconds() { return durationSeconds; }
  public void setDurationSeconds(long durationSeconds) { this.durationSeconds = durationSeconds; }
  public String getPromptText() { return promptText; }
  public void setPromptText(String promptText) { this.promptText = promptText; }
  public String getJustification() { return justification; }
  public void setJustification(String justification) { this.justification = justification; }
  public boolean isNoJustification() { return noJustification; }
  public void setNoJustification(boolean noJustification) { this.noJustification = noJustification; }
  public boolean isSubmitted() { return submitted; }
  public void setSubmitted(boolean submitted) { this.submitted = submitted; }
}
