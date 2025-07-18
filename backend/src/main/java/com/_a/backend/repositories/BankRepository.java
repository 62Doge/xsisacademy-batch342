package com._a.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    
    Boolean existsByNameIgnoreCaseAndIsDeleteFalse(String name);
    
    Boolean existsByVaCodeAndIsDeleteFalse(String vaCode);

    Optional<Bank> findByNameIgnoreCaseAndIsDeleteFalse(String name);

    Optional<Bank> findByVaCodeAndIsDeleteFalse(String name);

    List<Bank> findByNameContainingIgnoreCaseAndIsDeleteFalse(String name);
    
    Page<Bank> findByNameContainingIgnoreCaseAndIsDeleteFalse(String name, Pageable pageable);
    
    @Query(value = "select * from m_bank where is_delete = false", nativeQuery = true)
    List<Bank> findAllActive();

    Page<Bank> findAllByIsDeleteFalse(Pageable page);

}
