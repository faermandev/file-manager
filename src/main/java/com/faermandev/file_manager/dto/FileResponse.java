package com.faermandev.file_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

    private Long id;
    private String originalName;
    private String url;
    private String contentType;
    private Long size;
    private LocalDateTime uploadedAt;

}
