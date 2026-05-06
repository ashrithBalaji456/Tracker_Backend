package com.teamtaskmanager.repository;

import com.teamtaskmanager.model.Project;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  @EntityGraph(attributePaths = {"createdBy", "members"})
  List<Project> findDistinctByMembersIdOrCreatedById(Long memberId, Long creatorId);
}
