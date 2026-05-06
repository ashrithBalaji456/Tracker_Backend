package com.teamtaskmanager.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(length = 1000)
  private String description;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User createdBy;

  @ManyToMany
  @JoinTable(
      name = "project_members",
      joinColumns = @JoinColumn(name = "project_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> members = new HashSet<>();

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Task> tasks = new ArrayList<>();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public User getCreatedBy() { return createdBy; }
  public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
  public Set<User> getMembers() { return members; }
  public void setMembers(Set<User> members) { this.members = members; }
  public List<Task> getTasks() { return tasks; }
  public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
