package com.faermandev.file_manager.service;

import com.faermandev.file_manager.dto.LoginRequest;
import com.faermandev.file_manager.entity.User;
import com.faermandev.file_manager.exception.InvalidCredentialsException;
import com.faermandev.file_manager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid credentials")
                );
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );
        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return user;
    }
}