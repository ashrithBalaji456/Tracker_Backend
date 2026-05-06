package com.teamtaskmanager.repository;

import com.teamtaskmanager.model.WorkSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {
  Optional<WorkSession> findFirstByUserIdAndWorkDateOrderByPunchedInAtDesc(Long userId, LocalDate workDate);
  List<WorkSession> findByUserIdOrderByWorkDateDescPunchedInAtDesc(Long userId);
  List<WorkSession> findByWorkDateOrderByTaskingSecondsDesc(LocalDate workDate);
  List<WorkSession> findByProductivityFlaggedTrueOrderByWorkDateDescPunchedOutAtDesc();
}
