package com.faermandev.file_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "must not be blank")
    @Size(min = 3, max = 50, message = "name must be between 3 and 50 characters")
    private String name;

    @Email(message = "must be a well-formed email address")
    @NotBlank(message = "must not be blank")
    @Size(max = 255, message = "email must have at most 255 characters")
    private String email;

    @NotBlank(message = "must not be blank")
    @Size(min = 6, message = "password must have at least 6 characters")
    private String password;

}
