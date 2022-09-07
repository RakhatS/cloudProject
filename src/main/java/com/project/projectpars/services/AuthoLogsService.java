package com.project.projectpars.services;


import com.project.projectpars.models.AuthorizationLogs;
import com.project.projectpars.models.FileActiveLogs;
import com.project.projectpars.repo.AuthorizationLogsRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@Slf4j
@RequiredArgsConstructor
public class AuthoLogsService {
    private final AuthorizationLogsRepo authorizationLogsRepo;
    public List<AuthorizationLogs> getAllByUserId(Long id){
        return authorizationLogsRepo.findByUserId(id);
    }
    public void save(AuthorizationLogs authorizationLogs){
        authorizationLogsRepo.save(authorizationLogs);
    }
}
