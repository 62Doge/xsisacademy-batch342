package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.Biodata;

public interface BiodataRepository extends JpaRepository<Biodata, Long> {
    Boolean existsByFullname(String fullname);

    Page<Biodata> findByFullnameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String fullname);

    Page<Biodata> findAllByIsDeleteFalse(Pageable pageable);

    List<Biodata> findAllByIsDeleteFalse();
}
