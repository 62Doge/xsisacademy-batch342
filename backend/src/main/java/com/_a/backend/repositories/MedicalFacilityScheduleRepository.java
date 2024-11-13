package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.MedicalFacilitySchedule;

public interface MedicalFacilityScheduleRepository extends JpaRepository<MedicalFacilitySchedule, Long> {
    
}
