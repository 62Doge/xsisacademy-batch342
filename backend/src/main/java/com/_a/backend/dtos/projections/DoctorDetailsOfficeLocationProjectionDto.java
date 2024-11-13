package com._a.backend.dtos.projections;

public interface DoctorDetailsOfficeLocationProjectionDto {
    
    Long getMedicalFacilityId();
    String getMedicalFacilityName();
    String getServiceUnitName();
    String getAddress();
    String getSubdistrict();
    String getCity();

}
