package com._a.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.BloodGroup;

@Repository
public interface BloodGroupRepository extends JpaRepository<BloodGroup, Long> {

    Optional<BloodGroup> findByCodeIgnoreCaseAndIsDeleteFalse(String code);

    List<BloodGroup> findByCodeContainingIgnoreCaseAndIsDeleteFalse(String code);

    Boolean existsByCodeIgnoreCaseAndIsDeleteFalse(String code);

    @Query(value = "select * from m_blood_group where is_delete = false", nativeQuery = true)
    List<BloodGroup> findAllActive();

}
