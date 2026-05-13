package com.faermandev.file_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tokenType;
}
