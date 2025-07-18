package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.LocationLevel;

@Repository
public interface LocationLevelRepository extends JpaRepository<LocationLevel, Long> {
    Boolean existsByName(String name);
    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalse(String name);
    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalseAndIdNot(String name, long id);
    Page<LocationLevel> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);
    Page<LocationLevel> findAllByIsDeleteFalse(Pageable pageable);
    List<LocationLevel> findAllByIsDeleteFalse();

    @Query(value = "select ml.id, ml.name, ml.abbreviation, case when ml.modified_on is null then ml.created_on else ml.created_on end from m_location_level ml where ml.is_delete = false", nativeQuery = true)
    Page<LocationLevel> getLocationLevelWithCreatedOn(Pageable pageable);
}
