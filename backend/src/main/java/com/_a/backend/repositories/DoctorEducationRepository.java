package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.DoctorEducation;

@Repository
public interface DoctorEducationRepository extends JpaRepository<DoctorEducation, Long> {
  
  List<DoctorEducation>findByInstitutionNameContainingIgnoreCaseAndIsDeleteFalse(String institutionName);
  @Query(value = "SELECT * FROM m_doctor_education WHERE is_delete = false AND doctor_id = ?1 ORDER BY end_year DESC", nativeQuery = true)
  List<DoctorEducation> findByDoctorId(Long doctorId);
}
