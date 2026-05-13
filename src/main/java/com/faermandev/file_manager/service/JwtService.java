package com.faermandev.file_manager.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String SECRET_KEY = "my-super-secret-crazy-key";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String generateToken

}
