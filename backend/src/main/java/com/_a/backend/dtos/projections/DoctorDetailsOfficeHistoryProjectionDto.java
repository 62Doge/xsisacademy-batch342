package com._a.backend.dtos.projections;

import java.time.LocalDate;

public interface DoctorDetailsOfficeHistoryProjectionDto {
    
    String getName();
    String getLocation();
    String getSpecialization();
    LocalDate getStartDate();
    LocalDate getEndDate();

}
