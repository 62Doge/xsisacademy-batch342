package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class DoctorDetailsPriceStartResponseDTO {
    
    public DoctorDetailsPriceStartResponseDTO(Double priceStart) {
        this.priceStart = priceStart;
    }

    private Double priceStart;

}
