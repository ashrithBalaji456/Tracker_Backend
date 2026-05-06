package com.teamtaskmanager.repository;

import com.teamtaskmanager.model.TaskerFeedback;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskerFeedbackRepository extends JpaRepository<TaskerFeedback, Long> {
  List<TaskerFeedback> findByTaskerIdOrderByCreatedAtDesc(Long taskerId);
}
