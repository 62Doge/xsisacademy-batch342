package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.MedicalItemCategory;

public interface MedicalItemCategoryRepository extends JpaRepository<MedicalItemCategory, Long> {
    Boolean existsByName(String name);
    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalse(String name);
    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalseAndIdNot(String name, long id);
    Page<MedicalItemCategory> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);
    Page<MedicalItemCategory> findAllByIsDeleteFalse(Pageable pageable);
    List<MedicalItemCategory> findAllByIsDeleteFalse();
}
