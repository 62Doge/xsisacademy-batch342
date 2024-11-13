package com._a.backend.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.projections.DoctorDetailsEducationProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsHeaderProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsOfficeHistoryProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsOfficeLocationProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsPriceStartProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsScheduleProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsTreatmentProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsYOEProjectionDto;
import com._a.backend.dtos.responses.DoctorDetailsEducationResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsHeaderResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsOfficeHistoryResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsOfficeLocationResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsPriceStartResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsScheduleResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsTreatmentResponseDTO;
import com._a.backend.repositories.DoctorRepository;
import com._a.backend.services.DoctorDetailsService;

@Service
public class DoctorDetailsServiceImpl implements DoctorDetailsService {

    @Autowired
    DoctorRepository doctorRepository;

    @Override
    public DoctorDetailsHeaderResponseDTO getHeader(Long doctorId) {
        DoctorDetailsHeaderProjectionDto projection = doctorRepository.getHeader(doctorId);
        DoctorDetailsYOEProjectionDto yearOfExperience = doctorRepository.getYearOfExperience(doctorId);
        return new DoctorDetailsHeaderResponseDTO(
            projection.getName(), 
            projection.getSpecialization(),
            yearOfExperience.getYearOfExperience()
        );
    }
    
    @Override
    public DoctorDetailsTreatmentResponseDTO getTreatment(Long doctorId) {
        List<DoctorDetailsTreatmentProjectionDto> projections = doctorRepository.getTreatment(doctorId);
        return new DoctorDetailsTreatmentResponseDTO(
            projections.stream()
                .map(DoctorDetailsTreatmentProjectionDto::getName)
                .collect(Collectors.toList()));
    }

    @Override
    public DoctorDetailsEducationResponseDTO getEducation(Long doctorId) {
        List<DoctorDetailsEducationProjectionDto> projections = doctorRepository.getEducation(doctorId);
        List<DoctorDetailsEducationResponseDTO.EducationDetail> educationDetails = projections.stream()
            .map(projection -> new DoctorDetailsEducationResponseDTO.EducationDetail(
                projection.getName(),
                projection.getMajor(),
                projection.getYear()
            )).collect(Collectors.toList());
        return new DoctorDetailsEducationResponseDTO(educationDetails);
    }

    @Override
    public DoctorDetailsOfficeHistoryResponseDTO getOfficeHistory(Long doctorId) {
        List<DoctorDetailsOfficeHistoryProjectionDto> projections = doctorRepository.getOfficeHistory(doctorId);
        List<DoctorDetailsOfficeHistoryResponseDTO.OfficeHistory> officeHistories = projections.stream()
            .map(projection -> new DoctorDetailsOfficeHistoryResponseDTO.OfficeHistory(
                projection.getName(), 
                projection.getLocation(),
                projection.getSpecialization(), 
                projection.getStartDate(), 
                projection.getEndDate()
            )).collect(Collectors.toList());
        return new DoctorDetailsOfficeHistoryResponseDTO(officeHistories);
    }

    @Override
    public DoctorDetailsScheduleResponseDTO getSchedule(Long medicalFacilityId, Long doctorId) {
        List<DoctorDetailsScheduleProjectionDto> projections = doctorRepository.getSchedule(medicalFacilityId, doctorId);
        List<DoctorDetailsScheduleResponseDTO.Schedule> schedules = projections.stream()
            .map(projection -> new DoctorDetailsScheduleResponseDTO.Schedule(
                projection.getDay(), 
                projection.getStartTime(), 
                projection.getEndTime()
            )).collect(Collectors.toList());
        return new DoctorDetailsScheduleResponseDTO(schedules);
    }

    @Override
    public DoctorDetailsPriceStartResponseDTO getPriceStart(Long medicalFacilityId, Long doctorId) {
        DoctorDetailsPriceStartProjectionDto projection = doctorRepository.getPriceStart(medicalFacilityId, doctorId);
        return new DoctorDetailsPriceStartResponseDTO(projection.getPriceStart());
    }

    @Override
    public DoctorDetailsOfficeLocationResponseDTO getOfficeLocation(Long doctorId) {
        List<DoctorDetailsOfficeLocationProjectionDto> projections = doctorRepository.getOfficeLocation(doctorId);
        List<DoctorDetailsOfficeLocationResponseDTO.OfficeLocation> officeLocations = projections.stream()
            .map(projection -> new DoctorDetailsOfficeLocationResponseDTO.OfficeLocation(
                projection.getMedicalFacilityId(),
                projection.getMedicalFacilityName(), 
                projection.getServiceUnitName(), 
                projection.getAddress(), 
                projection.getSubdistrict(), 
                projection.getCity()
                )).collect(Collectors.toList());
        return new DoctorDetailsOfficeLocationResponseDTO(officeLocations);
    }
    
}
