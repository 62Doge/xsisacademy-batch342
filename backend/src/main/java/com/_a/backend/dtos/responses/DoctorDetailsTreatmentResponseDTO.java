package com._a.backend.dtos.responses;

import java.util.List;

import lombok.Data;

@Data
public class DoctorDetailsTreatmentResponseDTO {

    public DoctorDetailsTreatmentResponseDTO(List<String> treatments) {
        this.treatments = treatments;
    }

    public List<String> treatments;

}
