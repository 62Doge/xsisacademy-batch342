package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.BiodataAddress;

public interface BiodataAddressRepository extends JpaRepository<BiodataAddress, Long> {

    Page<BiodataAddress> findAllByIsDeleteFalse(Pageable pageable);

    List<BiodataAddress> findAllByIsDeleteFalse();

    @Query("SELECT b FROM BiodataAddress b WHERE b.isDelete = false AND "
            + "(LOWER(b.recipient) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(b.address) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<BiodataAddress> searchByKeyword(Pageable pageable, @Param("keyword") String keyword);

    Page<BiodataAddress> findAllByBiodataIdAndIsDeleteFalse(Pageable pageable, Long biodataId);

    List<BiodataAddress> findAllByBiodataIdAndIsDeleteFalse(Long biodataId);

    @Query("SELECT b FROM BiodataAddress b WHERE b.isDelete = false AND b.biodataId = :biodataId AND "
            + "(LOWER(b.recipient) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(b.address) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<BiodataAddress> searchByBiodataIdAndKeyword(Pageable pageable, @Param("biodataId") Long biodataId, @Param("keyword") String keyword);

    @Query("SELECT b.label FROM BiodataAddress b WHERE b.isDelete = false AND b.id IN (:ids)")
    List<String> findAllLabelsByBiodataIdInAndIsDeleteFalse(@Param("ids") List<Long> ids);

    Page<BiodataAddress> findAllByBiodataIdInAndIsDeleteFalse(Pageable pageable, List<Long> biodataIds);
}
