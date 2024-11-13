package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class DoctorDetailsHeaderResponseDTO {

    public DoctorDetailsHeaderResponseDTO(String name, String specialization, Integer yearOfExperience) {
        this.name = name;
        this.specialization = specialization;
        this.yearOfExperience = yearOfExperience;
    }

    private String name;
    private String specialization;
    private Integer yearOfExperience;

}
