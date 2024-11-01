package com._a.backend.repositories;

import com._a.backend.entities.LocationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationLevelRepository extends JpaRepository<LocationLevel, Long> {
    Boolean existsByName(String name);
    List<LocationLevel> findByNameContainingIgnoreCase(String name);
    //    Boolean existsByLocations(Locations locations);

}
