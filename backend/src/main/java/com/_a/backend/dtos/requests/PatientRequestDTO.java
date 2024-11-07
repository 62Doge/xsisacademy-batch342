package com._a.backend.dtos.requests;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PatientRequestDTO {
    
    private Long id;
    private Long parentBiodataId;
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String blood;
    private String rhesus;
    private Double height;
    private Double weight;
    private String relation;

}
