package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.DoctorTreatment;

@Repository
public interface DoctorTreatmentRepository extends JpaRepository<DoctorTreatment, Long> {
  @Query(value = "SELECT * FROM t_doctor_treatment WHERE is_delete = false AND doctor_id = ?1", nativeQuery = true)
  List<DoctorTreatment> findByDoctorId(Long doctorId);

  Boolean existsByName(String name);

  Page<DoctorTreatment> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

  Page<DoctorTreatment> findAllByIsDeleteFalse(Pageable pageable);

  List<DoctorTreatment> findAllByIsDeleteFalse();
}
