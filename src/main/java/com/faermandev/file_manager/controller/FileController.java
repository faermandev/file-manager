package com.faermandev.file_manager.controller;

import com.faermandev.file_manager.dto.FileResponse;
import com.faermandev.file_manager.entity.User;
import com.faermandev.file_manager.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/{id}/download")
    public ResponseEntity<String> downloadFile(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        String url = fileService.generateDownloadUrl(id, user.getId());
        return ResponseEntity.ok(url);
    }

    @GetMapping
    public ResponseEntity<List<FileResponse>> listFiles(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<FileResponse> files = fileService.listFiles(user.getId())
                .stream()
                .map(file -> new FileResponse(
                        file.getId(),
                        file.getOriginalName(),
                        file.getUrl(),
                        file.getContentType(),
                        file.getSize(),
                        file.getUploadedAt()
                ))
                .toList();

        return ResponseEntity.ok(files);
    }

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        FileResponse response = fileService.uploadFile(file, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> uploadFile(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        fileService.deleteFile(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
