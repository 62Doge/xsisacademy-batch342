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
