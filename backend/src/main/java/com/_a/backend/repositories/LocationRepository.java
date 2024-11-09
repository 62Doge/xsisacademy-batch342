package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Location l WHERE l.name = :name AND l.isDelete = false")
    Boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Location l WHERE l.name = :name AND l.parentId = :parentId AND l.isDelete = false")
    Boolean existsByNameAndParentId(@Param("name") String name, @Param("parentId") Long parentId);

    Page<Location> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    Page<Location> findByLocationLevelIdAndIsDeleteFalse(Pageable pageable, Long locationLevelId);

    Page<Location> findAllByIsDeleteFalse(Pageable pageable);

    Page<Location> findByLocationLevelIdAndNameContainingIgnoreCaseAndIsDeleteFalse(
            Pageable pageable, Long locationLevelId, String name);

    List<Location> findAllByIsDeleteFalse();

    @Query("SELECT l FROM Location l WHERE l.isDelete = false AND "
            + "(LOWER(l.locationLevel.abbreviation) LIKE LOWER(CONCAT('%kec%')) OR "
            + "LOWER(l.locationLevel.abbreviation) LIKE LOWER(CONCAT('%kota%')))")
    List<Location> findLocationKecamatanOrKota();
}
