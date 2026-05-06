package com.teamtaskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class CatalogProject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 220)
  private String name;

  @Column(nullable = false)
  private double minutesPerTask;

  @Column(nullable = false, length = 80)
  private String domain;

  @Column(nullable = false)
  private boolean justificationExpected;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User createdBy;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public double getMinutesPerTask() { return minutesPerTask; }
  public void setMinutesPerTask(double minutesPerTask) { this.minutesPerTask = minutesPerTask; }
  public String getDomain() { return domain; }
  public void setDomain(String domain) { this.domain = domain; }
  public boolean isJustificationExpected() { return justificationExpected; }
  public void setJustificationExpected(boolean justificationExpected) { this.justificationExpected = justificationExpected; }
  public User getCreatedBy() { return createdBy; }
  public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
