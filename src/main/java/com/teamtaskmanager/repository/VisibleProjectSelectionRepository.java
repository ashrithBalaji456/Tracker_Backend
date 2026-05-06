package com.teamtaskmanager.repository;

import com.teamtaskmanager.model.VisibleProjectSelection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface VisibleProjectSelectionRepository extends JpaRepository<VisibleProjectSelection, Long> {
  List<VisibleProjectSelection> findByUserIdOrderByProjectName(Long userId);
  boolean existsByUserIdAndProjectName(Long userId, String projectName);

  @Modifying
  @Query("delete from VisibleProjectSelection selection where selection.user.id = :userId")
  void deleteByUserId(Long userId);
}
