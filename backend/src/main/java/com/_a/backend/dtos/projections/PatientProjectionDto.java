package com._a.backend.dtos.projections;

import java.time.LocalDate;

public interface PatientProjectionDto {
    Long getId();
    Long getParentBiodataId();
    String getFullName();
    LocalDate getDob();
    String getGender();
    Long getBloodGroupId();
    String getRhesus();
    Long getCustomerRelationId();
}
