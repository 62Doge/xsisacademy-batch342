package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.DoctorOfficeTreatmentPrice;

@Repository
public interface DoctorOfficeTreatmentPriceRepository extends JpaRepository<DoctorOfficeTreatmentPrice, Long> {
    
}
