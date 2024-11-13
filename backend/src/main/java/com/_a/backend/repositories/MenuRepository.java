package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.dtos.responses.MenuResponseDTO;
import com._a.backend.entities.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END 
        FROM Menu m 
        WHERE m.name = :name 
        AND m.isDelete = FALSE
        """)
    Boolean existsByName(@Param("name") String name);

    @Query("""
        SELECT m 
        FROM Menu m 
        WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) 
        AND m.isDelete = FALSE
        """)
    Page<Menu> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, @Param("name") String name);

    @Query("""
        SELECT m 
        FROM Menu m 
        WHERE m.isDelete = FALSE
        """)
    Page<Menu> findAllByIsDeleteFalse(Pageable pageable);

    @Query("""
        SELECT m 
        FROM Menu m 
        WHERE m.isDelete = FALSE
        """)
    List<Menu> findAllByIsDeleteFalse();

    @Query("""
        SELECT m 
        FROM Menu m 
        LEFT JOIN MenuRole mr ON m.id = mr.menuId 
        WHERE mr.roleId = :roleId 
        AND m.isDelete = false 
        AND mr.isDelete = false 
        ORDER BY m.parentId DESC
        """)
    List<Menu> findMenusByRole(@Param("roleId") Long roleId);

    @Query("""
        SELECT m 
        FROM Menu m 
        LEFT JOIN MenuRole mr ON m.id = mr.menuId 
        WHERE mr.roleId = -1 
        AND m.isDelete = false 
        AND (mr.isDelete = false OR mr.roleId = -1) 
        ORDER BY mr.roleId
        """)
    List<Menu> findMenusByUniversalAccess();

    @Query("""
        SELECT m 
        FROM Menu m 
        LEFT JOIN MenuRole mr ON m.id = mr.menuId 
        WHERE (mr.roleId = :roleId OR mr.roleId = -1) 
        AND m.isDelete = false 
        AND (mr.isDelete = false OR mr.roleId = -1) 
        ORDER BY mr.roleId
        """)
    List<MenuResponseDTO> findMenusByRoleOrUniversalAccess(@Param("roleId") Long roleId);

}
