package com.teamtaskmanager.repository;

import com.teamtaskmanager.model.Task;
import com.teamtaskmanager.model.TaskStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
  @EntityGraph(attributePaths = {"assignedTo", "project"})
  List<Task> findByProjectMembersIdOrProjectCreatedById(Long memberId, Long creatorId);

  @EntityGraph(attributePaths = {"assignedTo", "project"})
  List<Task> findByAssignedToId(Long userId);

  long countByAssignedToIdAndStatus(Long userId, TaskStatus status);
  long countByAssignedToIdAndDueDateBeforeAndStatusNot(Long userId, LocalDate date, TaskStatus status);
}
