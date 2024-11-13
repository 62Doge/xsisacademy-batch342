package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.MedicalFacility;

public interface MedicalFacilityRepository extends JpaRepository<MedicalFacility, Long> {
    
}
