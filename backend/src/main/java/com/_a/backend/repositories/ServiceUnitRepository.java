package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.ServiceUnit;

public interface ServiceUnitRepository extends JpaRepository<ServiceUnit, Long> {
    
}
