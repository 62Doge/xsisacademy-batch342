package com._a.backend.dtos.responses;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BiodataResponseDTO {
    private Long id;
    private String fullname;
    private LocalDate dob;
    private String mobilePhone;
    private String email;
    private byte[] image;
    private String imagePath;
}
