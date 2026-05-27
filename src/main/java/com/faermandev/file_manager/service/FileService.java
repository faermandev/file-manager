package com.faermandev.file_manager.service;

import com.faermandev.file_manager.entity.File;
import com.faermandev.file_manager.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public List<File> listFiles(Long ownerId) {
        return fileRepository.findByOwner_Id(ownerId);
    }
}
