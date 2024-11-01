package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
    
}
