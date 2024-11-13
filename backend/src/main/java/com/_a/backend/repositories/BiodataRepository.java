package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.Biodata;

public interface BiodataRepository extends JpaRepository<Biodata, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END 
        FROM Biodata b 
        WHERE b.fullname = :fullname 
        AND b.isDelete = FALSE
        """)
    Boolean existsByFullname(@Param("fullname") String fullname);

    @Query("""
        SELECT b 
        FROM Biodata b 
        WHERE LOWER(b.fullname) LIKE LOWER(CONCAT('%', :fullname, '%')) 
        AND b.isDelete = FALSE
        """)
    Page<Biodata> findByFullnameContainingIgnoreCaseAndIsDeleteFalse(@Param("fullname") String fullname, Pageable pageable);

    @Query("""
        SELECT b 
        FROM Biodata b 
        WHERE b.isDelete = FALSE
        """)
    Page<Biodata> findAllByIsDeleteFalse(Pageable pageable);

    @Query("""
        SELECT b 
        FROM Biodata b 
        WHERE b.isDelete = FALSE
        """)
    List<Biodata> findAllByIsDeleteFalse();
}
