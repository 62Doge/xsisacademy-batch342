package com._a.backend.dtos.responses;

import java.time.LocalDate;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PatientResponseDTO {
    
    public PatientResponseDTO() {
        
    }

    public PatientResponseDTO(Long id, Long parentBiodataId, String fullName, LocalDate dob, String gender, Long bloodGroupId, String rhesus, Double height, Double weight, Long customerRelationId) {
        this.id = id;
        this.parentBiodataId = parentBiodataId;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.bloodGroupId = bloodGroupId;
        this.rhesus = rhesus;
        this.height = height;
        this.weight = weight;
        this.customerRelationId = customerRelationId;
    }

    // customer member id
    private Long id;

    @Column(name = "parent_biodata_id")
    private Long parentBiodataId;
    
    @Column(name = "fullname")
    private String fullName;
    
    private LocalDate dob;

    private String gender;
    
    @Column(name = "blood_group_id")
    private Long bloodGroupId;
    
    @Column(name = "rhesus_type")
    private String rhesus;

    private Double height;

    private Double weight;
    
    @Column(name = "customer_relation_id")
    private Long customerRelationId;

}
