package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.MedicalItem;

public interface MedicalItemRepository extends JpaRepository<MedicalItem, Long> {

    Boolean existsByName(String name);

    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalse(String name);

    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalseAndIdNot(String name, long id);

    Page<MedicalItem> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    Page<MedicalItem> findAllByIsDeleteFalse(Pageable pageable);

    List<MedicalItem> findAllByIsDeleteFalse();

    @Query("SELECT m FROM MedicalItem m "
            + "JOIN m.medicalItemSegmentation seg "
            + "WHERE (:medicalItemCategoryId IS NULL OR m.medicalItemCategoryId = :medicalItemCategoryId) "
            + "AND (:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword , '%')) OR LOWER(m.indication) LIKE LOWER(CONCAT('%', :keyword , '%'))) "
            + "AND (:segmentationName IS NULL OR LOWER(seg.name) LIKE LOWER(CONCAT('%', :segmentationName , '%'))) "
            + "AND (:minPrice IS NULL OR m.priceMin >= :minPrice) "
            + "AND (:maxPrice IS NULL OR m.priceMax <= :maxPrice) "
            + "AND (m.isDelete = FALSE)")
    Page<MedicalItem> searchMedicalItems(
            Pageable pageable,
            @Param("medicalItemCategoryId") Long medicalItemCategoryId,
            @Param("keyword") String keyword,
            @Param("segmentationName") String segmentationName,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

    @Query("SELECT m FROM MedicalItem m "
            + "JOIN m.medicalItemSegmentation seg "
            + "JOIN m.medicalItemCategory cat "
            + "WHERE (:categoryName IS NULL OR LOWER(cat.name) LIKE LOWER(:categoryName)) "
            + "AND (:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword , '%')) OR LOWER(m.indication) LIKE LOWER(CONCAT('%', :keyword , '%'))) "
            + "AND (:segmentationName IS NULL OR LOWER(seg.name) LIKE LOWER(CONCAT('%', :segmentationName , '%'))) "
            + "AND (:minPrice IS NULL OR m.priceMin >= :minPrice) "
            + "AND (:maxPrice IS NULL OR m.priceMax <= :maxPrice) "
            + "AND (m.isDelete = FALSE)")
    Page<MedicalItem> searchMedicalItemsVersionTwo(
            Pageable pageable,
            @Param("categoryName") String categoryName,
            @Param("keyword") String keyword,
            @Param("segmentationName") String segmentationName,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
