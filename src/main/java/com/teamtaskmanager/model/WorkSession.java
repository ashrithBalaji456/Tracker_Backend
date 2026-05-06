package com.teamtaskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class WorkSession {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User user;

  @Column(nullable = false)
  private LocalDate workDate;

  @Column(nullable = false)
  private LocalDateTime punchedInAt;

  private LocalDateTime currentLoginStartedAt;

  private LocalDateTime punchedOutAt;

  @Column(nullable = false)
  private long taskingSeconds = 0;

  private Long accumulatedLoginSeconds = 0L;

  private Boolean repunchAllowed = false;

  @Column(length = 5000)
  private String visibleProjectSnapshot = "";

  private Long expectedTaskingSeconds = 0L;

  private Boolean productivityFlagged = false;

  private Boolean warningAcknowledged = false;

  @Column(length = 500)
  private String warningAcknowledgedReason = "";

  @Column(length = 500)
  private String productivityFlagReason = "";

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public LocalDate getWorkDate() { return workDate; }
  public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }
  public LocalDateTime getPunchedInAt() { return punchedInAt; }
  public void setPunchedInAt(LocalDateTime punchedInAt) { this.punchedInAt = punchedInAt; }
  public LocalDateTime getCurrentLoginStartedAt() { return currentLoginStartedAt == null ? punchedInAt : currentLoginStartedAt; }
  public void setCurrentLoginStartedAt(LocalDateTime currentLoginStartedAt) { this.currentLoginStartedAt = currentLoginStartedAt; }
  public LocalDateTime getPunchedOutAt() { return punchedOutAt; }
  public void setPunchedOutAt(LocalDateTime punchedOutAt) { this.punchedOutAt = punchedOutAt; }
  public long getTaskingSeconds() { return taskingSeconds; }
  public void setTaskingSeconds(long taskingSeconds) { this.taskingSeconds = taskingSeconds; }
  public long getAccumulatedLoginSeconds() { return accumulatedLoginSeconds == null ? 0 : accumulatedLoginSeconds; }
  public void setAccumulatedLoginSeconds(long accumulatedLoginSeconds) { this.accumulatedLoginSeconds = accumulatedLoginSeconds; }
  public boolean isRepunchAllowed() { return Boolean.TRUE.equals(repunchAllowed); }
  public void setRepunchAllowed(boolean repunchAllowed) { this.repunchAllowed = repunchAllowed; }
  public String getVisibleProjectSnapshot() { return visibleProjectSnapshot == null ? "" : visibleProjectSnapshot; }
  public void setVisibleProjectSnapshot(String visibleProjectSnapshot) { this.visibleProjectSnapshot = visibleProjectSnapshot; }
  public long getExpectedTaskingSeconds() { return expectedTaskingSeconds == null ? 0 : expectedTaskingSeconds; }
  public void setExpectedTaskingSeconds(long expectedTaskingSeconds) { this.expectedTaskingSeconds = expectedTaskingSeconds; }
  public boolean isProductivityFlagged() { return Boolean.TRUE.equals(productivityFlagged); }
  public void setProductivityFlagged(boolean productivityFlagged) { this.productivityFlagged = productivityFlagged; }
  public boolean isWarningAcknowledged() { return Boolean.TRUE.equals(warningAcknowledged); }
  public void setWarningAcknowledged(boolean warningAcknowledged) { this.warningAcknowledged = warningAcknowledged; }
  public String getWarningAcknowledgedReason() { return warningAcknowledgedReason == null ? "" : warningAcknowledgedReason; }
  public void setWarningAcknowledgedReason(String warningAcknowledgedReason) { this.warningAcknowledgedReason = warningAcknowledgedReason; }
  public String getProductivityFlagReason() { return productivityFlagReason == null ? "" : productivityFlagReason; }
  public void setProductivityFlagReason(String productivityFlagReason) { this.productivityFlagReason = productivityFlagReason; }
}
