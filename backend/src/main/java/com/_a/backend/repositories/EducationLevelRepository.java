package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.EducationLevel;

public interface EducationLevelRepository extends JpaRepository<EducationLevel, Long> {
    
}
