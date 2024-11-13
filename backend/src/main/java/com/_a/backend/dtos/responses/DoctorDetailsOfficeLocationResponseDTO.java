package com._a.backend.dtos.responses;

import java.util.List;

import lombok.Data;

@Data
public class DoctorDetailsOfficeLocationResponseDTO {
    
    public DoctorDetailsOfficeLocationResponseDTO(List<OfficeLocation> officeLocation) {
        this.officeLocation = officeLocation;
    }

    private List<OfficeLocation> officeLocation;

    @Data
    public static class OfficeLocation {
        public OfficeLocation(Long medicalFacilityId, String medicalFacilityName, String serviceUnitName, String address, String subdistrict, String city) {
            this.medicalFacilityId = medicalFacilityId;
            this.medicalFacilityName = medicalFacilityName;
            this.serviceUnitName = serviceUnitName;
            this.address = address;
            this.subdistrict = subdistrict;
            this.city = city;
        }

        private Long medicalFacilityId;
        private String medicalFacilityName;
        private String serviceUnitName;
        private String address;
        private String subdistrict;
        private String city;
    }

}
