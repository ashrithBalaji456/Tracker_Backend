package com.teamtaskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class LeaveRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User tasker;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @Column(nullable = false, length = 1200)
  private String reason;

  @Column(nullable = false, length = 30)
  private String status = "PENDING";

  @Column(nullable = false)
  private LocalDateTime requestedAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public User getTasker() { return tasker; }
  public void setTasker(User tasker) { this.tasker = tasker; }
  public LocalDate getStartDate() { return startDate; }
  public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
  public LocalDate getEndDate() { return endDate; }
  public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
  public String getReason() { return reason; }
  public void setReason(String reason) { this.reason = reason; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public LocalDateTime getRequestedAt() { return requestedAt; }
  public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}
