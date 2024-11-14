package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class DoctorDetailsHeaderResponseDTO {

    public DoctorDetailsHeaderResponseDTO(String name, String specialization, Integer yearOfExperience, String image) {
        this.name = name;
        this.specialization = specialization;
        this.yearOfExperience = yearOfExperience;
        this.image = image;
    }

    private String name;
    private String specialization;
    private Integer yearOfExperience;
    private String image;

}
