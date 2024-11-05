package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.LocationLevel;

@Repository
public interface LocationLevelRepository extends JpaRepository<LocationLevel, Long> {
    Boolean existsByName(String name);

    Page<LocationLevel> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    Page<LocationLevel> findAllByIsDeleteFalse(Pageable pageable);

    List<LocationLevel> findAllByIsDeleteFalse();
}
