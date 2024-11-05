package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.BloodGroup;

@Repository
public interface BloodGroupRepository extends JpaRepository<BloodGroup, Long> {

}
