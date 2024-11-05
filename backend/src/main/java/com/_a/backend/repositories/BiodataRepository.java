package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.Biodata;

public interface BiodataRepository extends JpaRepository<Biodata, Long> {
    Boolean existsByName(String name);

    Page<Biodata> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    Page<Biodata> findAllByIsDeleteFalse(Pageable pageable);

    List<Biodata> findAllByIsDeleteFalse();
}
