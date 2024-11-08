package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._a.backend.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByName(String name);

    Page<Role> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    Page<Role> findAllByIsDeleteFalse(Pageable pageable);

    List<Role> findAllByIsDeleteFalse();

    @Query("""
            select r
            from Role r
            where
            (
                :name is null
                or lower(r.name) like %:name%
            )
            and (
                :code is null
                or lower(r.code) like %:code%
            )
            and r.isDelete=false
            """)
    Page<Role> findAllWithSearch(
            @Param("name") String name,
            @Param("code") String code,
            Pageable pageable);
}
