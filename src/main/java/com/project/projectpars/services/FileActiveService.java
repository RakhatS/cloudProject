package com.project.projectpars.services;

import com.project.projectpars.models.FileActiveLogs;
import com.project.projectpars.repo.FileActiveRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileActiveService {
    private final FileActiveRepo fileActiveRepo;


    public List<FileActiveLogs> getAllByUserId(Long id){
        return fileActiveRepo.findByUserId(id);
    }
    public void save(FileActiveLogs fileActiveLogs){
        fileActiveRepo.save(fileActiveLogs);
    }
}
