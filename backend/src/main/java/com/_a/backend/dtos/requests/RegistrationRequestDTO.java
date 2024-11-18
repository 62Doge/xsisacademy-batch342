package com._a.backend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequestDTO {
    private Long biodataId;
    private Long roleId;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank(message = "Nama lengkap wajib diisi")
    private String fullName;
    @Size(min = 10, max = 15, message = "Panjang nomor harus diantara 10 atau 15")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Pastikan nomor handphone valid")
    private String mobilePhone;
}
