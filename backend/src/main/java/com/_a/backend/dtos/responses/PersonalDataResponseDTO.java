package com._a.backend.dtos.responses;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonalDataResponseDTO {
    private Long id;
    private String fullName;
    private LocalDate dob;
    private String mobilePhone;
}
