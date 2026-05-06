package com.teamtaskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class TaskerFeedback {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User tasker;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User sentBy;

  @Column(nullable = false, length = 1200)
  private String message;

  @Column(nullable = false)
  private int rating;

  @Column(nullable = true)
  private LocalDate workDate;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public User getTasker() { return tasker; }
  public void setTasker(User tasker) { this.tasker = tasker; }
  public User getSentBy() { return sentBy; }
  public void setSentBy(User sentBy) { this.sentBy = sentBy; }
  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }
  public int getRating() { return rating; }
  public void setRating(int rating) { this.rating = rating; }
  public LocalDate getWorkDate() { return workDate; }
  public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
