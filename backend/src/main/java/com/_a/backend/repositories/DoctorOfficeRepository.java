package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.DoctorOffice;

@Repository
public interface DoctorOfficeRepository extends JpaRepository<DoctorOffice, Long> {
  @Query(value = "SELECT * FROM t_doctor_office WHERE is_delete = false AND doctor_id = ?1 ORDER BY start_date DESC", nativeQuery = true)
  List<DoctorOffice> findByDoctorId(Long doctorId);
}
