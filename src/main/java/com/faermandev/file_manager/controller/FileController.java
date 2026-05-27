package com.faermandev.file_manager.controller;

import com.faermandev.file_manager.dto.FileResponse;
import com.faermandev.file_manager.entity.User;
import com.faermandev.file_manager.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

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
                )).toList();

        return ResponseEntity
                .ok(files);
    }

}
