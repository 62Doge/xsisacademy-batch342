package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.MenuRole;

public interface MenuRoleRepository extends JpaRepository<MenuRole, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(mr) > 0 THEN TRUE ELSE FALSE END 
        FROM MenuRole mr 
        WHERE mr.menuId = :menuId 
        AND mr.isDelete = FALSE
        """)
    Boolean existsByMenuId(@Param("menuId") Long menuId);

    @Query("""
        SELECT mr 
        FROM MenuRole mr 
        WHERE mr.menuId = :menuId 
        AND mr.isDelete = FALSE
        """)
    Page<MenuRole> findByMenuIdContainingIgnoreCaseAndIsDeleteFalse(@Param("menuId") Long menuId, Pageable pageable);

    @Query("""
        SELECT mr 
        FROM MenuRole mr 
        WHERE mr.isDelete = FALSE
        """)
    Page<MenuRole> findAllByIsDeleteFalse(Pageable pageable);

    @Query("""
        SELECT mr 
        FROM MenuRole mr 
        WHERE mr.isDelete = FALSE
        """)
    List<MenuRole> findAllByIsDeleteFalse();

    @Query("""
        SELECT mr 
        FROM MenuRole mr 
        WHERE mr.roleId = :roleId
        AND mr.isDelete = FALSE
        """)
    List<MenuRole> findByRoleId(@Param("roleId") Long roleId);
}
