package com._a.backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordRegistRequestDTO {
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Confirmation Password is mandatory")
    private String confirmPassword;
}
