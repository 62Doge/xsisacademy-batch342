package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Boolean existsByName(String name);

    Boolean existsByNameAndParentId(String name, Long parentId);

    Page<Location> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    Page<Location> findByLocationLevelIdAndIsDeleteFalse(Pageable pageable, Long locationLevelId);

    Page<Location> findAllByIsDeleteFalse(Pageable pageable);

    Page<Location> findByLocationLevelIdAndNameContainingIgnoreCaseAndIsDeleteFalse(
            Pageable pageable, Long locationLevelId, String name);

    List<Location> findAllByIsDeleteFalse();
}
