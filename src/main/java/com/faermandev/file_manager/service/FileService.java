package com.faermandev.file_manager.service;

import com.faermandev.file_manager.dto.FileResponse;
import com.faermandev.file_manager.entity.File;
import com.faermandev.file_manager.entity.User;
import com.faermandev.file_manager.exception.FileNotFoundException;
import com.faermandev.file_manager.exception.UnauthorizedFileAccessException;
import com.faermandev.file_manager.exception.UserNotFoundException;
import com.faermandev.file_manager.repository.FileRepository;
import com.faermandev.file_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    public List<File> listFiles(Long ownerId) {
        return fileRepository.findByOwner_Id(ownerId);
    }

    public FileResponse uploadFile(MultipartFile multipartFile, Long ownerId) throws IOException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));

        String key = "uploads/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        String url = s3Service.uploadFile(key, multipartFile.getBytes(), multipartFile.getContentType());

        File file = new File();
        file.setOriginalName(multipartFile.getOriginalFilename());
        file.setS3Key(key);
        file.setUrl(url);
        file.setContentType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        file.setOwner(owner);

        File saved = fileRepository.save(file);

        return new FileResponse(
                saved.getId(),
                saved.getOriginalName(),
                saved.getUrl(),
                saved.getContentType(),
                saved.getSize(),
                saved.getUploadedAt()
        );
    }

    public void deleteFile(Long fileId, Long ownerId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        if (!file.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedFileAccessException("You don't have permission to delete this file");
        }

        s3Service.deleteFile(file.getS3Key());
        fileRepository.delete(file);
    }

    public String generateDownloadUrl(Long fileId, Long ownerId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        if (!file.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedFileAccessException("You don't have permission to access this file");
        }

        return s3Service.generatePresignedUrl(file.getS3Key());
    }
}
