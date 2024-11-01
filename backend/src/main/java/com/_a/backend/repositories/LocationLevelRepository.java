package com._a.backend.repositories;

import com._a.backend.entities.LocationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationLevelRepository extends JpaRepository<LocationLevel, Long> {
    Boolean existsByName(String name);

    //    Boolean existsByLocations(Locations locations);
}
