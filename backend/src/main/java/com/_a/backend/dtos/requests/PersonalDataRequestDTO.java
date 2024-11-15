package com._a.backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonalDataRequestDTO {
    @NotBlank(message = "Nama lengkap wajib diisi")
    private String fullName;
    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    private LocalDate dob;
    @NotBlank(message = "Nomor handphone tidak boleh kosong")
    @Size(min = 10, max = 15, message = "Panjang nomor harus diantara 10 atau 15")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Pastikan nomor handphone valid")
    private String mobilePhone;
}
