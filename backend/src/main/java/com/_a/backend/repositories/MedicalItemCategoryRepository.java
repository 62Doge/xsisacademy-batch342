package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.MedicalItemCategory;

public interface MedicalItemCategoryRepository extends JpaRepository<MedicalItemCategory, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM MedicalItemCategory c WHERE c.name = :name
        """)
    Boolean existsByName(@Param("name") String name);

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END 
        FROM MedicalItemCategory c WHERE LOWER(c.name) LIKE LOWER(:name) AND c.isDelete = false
        """)
    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalse(@Param("name") String name);

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END 
        FROM MedicalItemCategory c WHERE LOWER(c.name) LIKE LOWER(:name) 
        AND c.isDelete = false AND c.id != :id
        """)
    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalseAndIdNot(@Param("name") String name, @Param("id") long id);

    @Query("""
        SELECT c FROM MedicalItemCategory c WHERE LOWER(c.name) LIKE LOWER(:name) AND c.isDelete = false
        """)
    Page<MedicalItemCategory> findByNameContainingIgnoreCaseAndIsDeleteFalse(@Param("name") String name, Pageable pageable);

    @Query("""
        SELECT c FROM MedicalItemCategory c WHERE c.isDelete = false
        """)
    Page<MedicalItemCategory> findAllByIsDeleteFalse(Pageable pageable);

    @Query("""
        SELECT c FROM MedicalItemCategory c WHERE c.isDelete = false
        """)
    List<MedicalItemCategory> findAllByIsDeleteFalse();
}
