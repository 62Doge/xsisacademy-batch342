package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  @Query(value = "SELECT * FROM t_appointment WHERE is_delete = false AND doctor_office_id = ?1 AND appointment_date > NOW()", nativeQuery = true)
  List<Appointment> findByOfficeId(Long id);
}
