package com.teamtaskmanager.repository;

import com.teamtaskmanager.model.LeaveRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
  List<LeaveRequest> findAllByOrderByRequestedAtDesc();
  List<LeaveRequest> findByTaskerIdOrderByRequestedAtDesc(Long taskerId);
}
