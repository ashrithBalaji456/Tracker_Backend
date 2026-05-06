package com.teamtaskmanager.repository;

import com.teamtaskmanager.model.TaskLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {
  Optional<TaskLog> findFirstByUserIdAndSubmittedFalseOrderByStartedAtDesc(Long userId);
  Optional<TaskLog> findFirstByUserIdAndWorkDateAndSubmittedFalseOrderByStartedAtDesc(Long userId, LocalDate workDate);
  List<TaskLog> findByUserIdAndWorkDateOrderByStartedAtDesc(Long userId, LocalDate workDate);
}
