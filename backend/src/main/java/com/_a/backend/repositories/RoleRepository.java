package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END 
        FROM Role r 
        WHERE r.name = :name 
        AND r.isDelete = FALSE
        """)
    Boolean existsByName(@Param("name") String name);

    @Query("""
        SELECT r 
        FROM Role r 
        WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) 
        AND r.isDelete = FALSE
        """)
    Page<Role> findByNameContainingIgnoreCaseAndIsDeleteFalse(@Param("pageable") Pageable pageable, @Param("name") String name);

    @Query("""
        SELECT r 
        FROM Role r 
        WHERE r.isDelete = FALSE
        """)
    Page<Role> findAllByIsDeleteFalse(@Param("pageable") Pageable pageable);

    @Query("""
        SELECT r 
        FROM Role r 
        WHERE r.isDelete = FALSE
        """)
    List<Role> findAllByIsDeleteFalse();

    @Query("""
            select r
            from Role r
            where
            (
                :searchText is null
                or lower(r.name) like lower(concat('%', :searchText, '%'))
            )
            or (
                :searchText is null
                or lower(r.code) like lower(concat('%', :searchText, '%'))
            )
            and r.isDelete=false
            """)
    Page<Role> findAllWithSearch(
            @Param("searchText") String searchText,
            Pageable pageable);
}
