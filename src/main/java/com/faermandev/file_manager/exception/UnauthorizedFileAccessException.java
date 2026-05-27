package com.faermandev.file_manager.exception;

public class UnauthorizedFileAccessException extends RuntimeException {
    public UnauthorizedFileAccessException(String message) {
        super(message);
    }
}
