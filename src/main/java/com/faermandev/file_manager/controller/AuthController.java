package com.faermandev.file_manager.controller;

import com.faermandev.file_manager.dto.LoginRequest;
import com.faermandev.file_manager.dto.LoginResponse;
import com.faermandev.file_manager.dto.UserResponse;
import com.faermandev.file_manager.entity.User;
import com.faermandev.file_manager.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {

        User user = authService.login(request);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }

}
