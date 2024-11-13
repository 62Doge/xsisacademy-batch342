package com._a.backend.dtos.responses;

import java.util.List;

import lombok.Data;

@Data
public class DoctorDetailsEducationResponseDTO {

    public DoctorDetailsEducationResponseDTO(List<EducationDetail> education) {
        this.education = education;
    }

    private List<EducationDetail> education;

    @Data
    public static class EducationDetail {
        public EducationDetail(String name, String major, Integer year) {
            this.name = name;
            this.major = major;
            this.year = year;
        }

        private String name;
        private String major;
        private Integer year;
    }

}
