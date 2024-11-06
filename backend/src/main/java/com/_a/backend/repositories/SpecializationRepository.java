package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.Specialization;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long>{
  @Query(value = "SELECT * FROM m_specialization WHERE is_delete = false", nativeQuery = true)
  List<Specialization> findAllActive();
}
