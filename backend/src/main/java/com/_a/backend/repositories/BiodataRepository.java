package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.Biodata;

public interface BiodataRepository extends JpaRepository<Biodata, Long> {

}
