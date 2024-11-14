package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = """
        SELECT CASE WHEN COUNT(l) > 0 
        THEN true ELSE false END 
        FROM Location l 
        WHERE l.name = :name 
        AND l.isDelete = false
        """)
    Boolean existsByName(@Param("name") String name);

    @Query(value = """
        SELECT CASE WHEN COUNT(l) > 0 
        THEN true ELSE false END 
        FROM Location l 
        WHERE l.name = :name 
        AND l.parentId = :parentId 
        AND l.isDelete = false
        """)
    Boolean existsByNameAndParentId(@Param("name") String name, @Param("parentId") Long parentId);

    // @Query(value = """
    //     SELECT l
    //     FROM Location l
    //     WHERE LOWER(l.name) = LOWER(:name)
    //     AND l.isDelete = false
    //     """)
    Page<Location> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    // @Query(value = """
    // SELECT l
    // FROM Location l
    // WHERE l.locationLevel.id = :locationLevelId
    // AND l.isDelete = false
    // """)
    Page<Location> findByLocationLevelIdAndIsDeleteFalse(Pageable pageable, @Param("locationLevelId") Long locationLevelId);

    @Query(value = """
    SELECT l
    FROM Location l
    WHERE l.isDelete = false
    """)
    Page<Location> findAllByIsDeleteFalse(Pageable pageable);

    //search minimum huruf, muncul dialog
    // @Query(value = """
    // SELECT l
    // FROM Location l
    // WHERE l.locationLevel.id = :locationLevelId
    // AND LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%'))
    // AND l.isDelete = false
    // """)
    Page<Location> findByLocationLevelIdAndNameContainingIgnoreCaseAndIsDeleteFalse(
            Pageable pageable, @Param("locationLevelId") Long locationLevelId, @Param("name") String name);

    @Query(value = """
    SELECT l
    FROM Location l
    WHERE l.isDelete = false
    """)
    List<Location> findAllByIsDeleteFalse();

    @Query(value = """
        SELECT l 
        FROM Location l 
        WHERE l.isDelete = false 
        AND (LOWER(l.locationLevel.abbreviation) LIKE LOWER(CONCAT('%kec%')) 
        OR LOWER(l.locationLevel.abbreviation) LIKE LOWER(CONCAT('%kota%')))         
        """)
    List<Location> findLocationKecamatanOrKota();
}
