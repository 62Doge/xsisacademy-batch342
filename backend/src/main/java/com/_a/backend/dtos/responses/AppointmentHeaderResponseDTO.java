package com._a.backend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentHeaderResponseDTO {
    private String name;
    private String specialization;
    private Integer monthOfExperience;
}
