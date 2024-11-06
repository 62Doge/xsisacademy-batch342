package com._a.backend.dtos.responses;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PatientResponseDTO {
    
    private Long id;
    private Long parentBiodataId;
    private String fullName;
    private LocalDate dob;
    private String gender;
    private Long bloodGroupId;
    private String rhesus;
    private Long customerRelationId;

}
