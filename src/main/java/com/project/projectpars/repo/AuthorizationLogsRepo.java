package com.project.projectpars.repo;

import com.project.projectpars.models.AuthorizationLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorizationLogsRepo extends JpaRepository<AuthorizationLogs,Long> {
    List<AuthorizationLogs> findByUserId(Long id);
}
