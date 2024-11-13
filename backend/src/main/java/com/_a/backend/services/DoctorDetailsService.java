package com._a.backend.services;

import com._a.backend.dtos.responses.DoctorDetailsEducationResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsHeaderResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsOfficeHistoryResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsOfficeLocationResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsPriceStartResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsScheduleResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsTreatmentResponseDTO;

public interface DoctorDetailsService {
    
    DoctorDetailsHeaderResponseDTO getHeader(Long doctorId);

    DoctorDetailsTreatmentResponseDTO getTreatment(Long doctorId);

    DoctorDetailsEducationResponseDTO getEducation(Long doctorId);

    DoctorDetailsOfficeHistoryResponseDTO getOfficeHistory(Long doctorId);

    DoctorDetailsScheduleResponseDTO getSchedule(Long doctorId, Long medicalFacilityId);

    DoctorDetailsPriceStartResponseDTO getPriceStart(Long doctorId, Long medicalFacilityId);

    DoctorDetailsOfficeLocationResponseDTO getOfficeLocation(Long doctorId);

}
