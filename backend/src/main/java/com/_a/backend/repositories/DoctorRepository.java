package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
  @Query(value= "SELECT * FROM m_doctor WHERE is_delete = false AND id = ?1", nativeQuery = true)
  List<Doctor> findByDoctorId(Long id);
  Page<Doctor> findAllByIsDeleteFalse(Pageable pageable);
}
