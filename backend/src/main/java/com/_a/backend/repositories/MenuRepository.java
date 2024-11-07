package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Boolean existsByName(String name);

    Page<Menu> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    Page<Menu> findAllByIsDeleteFalse(Pageable pageable);

    List<Menu> findAllByIsDeleteFalse();

    @Query("SELECT m FROM Menu m " +
            "LEFT JOIN MenuRole mr ON m.id = mr.menuId " +
            "WHERE mr.roleId = :roleId " +
            "AND m.isDelete = false AND mr.isDelete = false " +
            "ORDER BY mr.roleId")
    List<Menu> findMenusByRole(@Param("roleId") Long roleId);

    @Query("SELECT m FROM Menu m " +
            "LEFT JOIN MenuRole mr ON m.id = mr.menuId " +
            "WHERE mr.roleId = -1 " +
            "AND m.isDelete = false AND (mr.isDelete = false OR mr.roleId = -1) " +
            "ORDER BY mr.roleId")
    List<Menu> findMenusByUniversalAccess();

}
