package com.teamtaskmanager.repository;

import com.teamtaskmanager.model.MissingProjectRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingProjectRequestRepository extends JpaRepository<MissingProjectRequest, Long> {
  List<MissingProjectRequest> findByResolvedFalseOrderByRequestedAtDesc();
  List<MissingProjectRequest> findByProjectNameIgnoreCaseAndResolvedFalse(String projectName);
}
