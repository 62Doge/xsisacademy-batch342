package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.DoctorOfficeSchedule;

@Repository
public interface DoctorOfficeScheduleRepository extends JpaRepository<DoctorOfficeSchedule, Long> {
  @Query("""
      select dos
      from DoctorOfficeSchedule dos
      join dos.doctor d
      join dos.medicalFacilitySchedule mfs
      where d.id=?1
      and dos.isDelete=false
      """)
  List<DoctorOfficeSchedule> findByDoctorId(Long doctorId);

}
