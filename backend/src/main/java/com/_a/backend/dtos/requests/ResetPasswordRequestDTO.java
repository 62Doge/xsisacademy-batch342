package com._a.backend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequestDTO {
  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String otp;
  @NotBlank
  private String newPassword;
  @NotBlank
  private String confirmPassword;
}
