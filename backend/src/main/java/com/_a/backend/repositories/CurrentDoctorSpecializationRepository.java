package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.CurrentDoctorSpecialization;

@Repository
public interface CurrentDoctorSpecializationRepository extends JpaRepository<CurrentDoctorSpecialization, Long> {
  @Query(value= "SELECT * FROM t_current_doctor_specialization WHERE is_delete = false AND doctor_id = ?1", nativeQuery = true)
  List<CurrentDoctorSpecialization> findByDoctorId(Long id);
  Page<CurrentDoctorSpecialization> findAllByIsDeleteFalse(Pageable pageable);
}
