package com.project.projectpars.repo;

import com.project.projectpars.models.FileActiveLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileActiveRepo extends JpaRepository<FileActiveLogs,Long> {
    List<FileActiveLogs> findByUserId(Long id);
}
