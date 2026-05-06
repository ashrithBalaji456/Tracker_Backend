package com.teamtaskmanager.repository;

import com.teamtaskmanager.model.CatalogProject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogProjectRepository extends JpaRepository<CatalogProject, Long> {
  List<CatalogProject> findAllByOrderByDomainAscNameAsc();
  Optional<CatalogProject> findByName(String name);
  boolean existsByName(String name);
}
