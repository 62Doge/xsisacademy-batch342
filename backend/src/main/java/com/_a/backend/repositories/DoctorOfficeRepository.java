package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.DoctorOffice;

@Repository
public interface DoctorOfficeRepository extends JpaRepository<DoctorOffice, Long> {
  @Query(value = "SELECT * FROM t_doctor_office WHERE is_delete = false AND doctor_id = ?1 ORDER BY start_date DESC", nativeQuery = true)
  List<DoctorOffice> findByDoctorId(Long doctorId);

  Boolean existsBySpecialization(String specialization);

  Page<DoctorOffice> findBySpecializationContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String specialization);

  Page<DoctorOffice> findAllByIsDeleteFalse(Pageable pageable);

  List<DoctorOffice> findAllByIsDeleteFalse();

  @Query("""
        SELECT do
        FROM DoctorOffice do
        join do.doctor d
        join d.doctorOfficeSchedules dos
        WHERE do.doctor.id = ?1
        and dos.doctorId = ?1
        AND do.isDelete=false
      """)
  List<DoctorOffice> findByDoctorIdJpql(Long doctorId);

}
