package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    
    Boolean existsByName(String name);
    
    Boolean existsByVaCode(String vaCode);

    List<Bank> findByNameContainingIgnoreCaseAndIsDeleteFalse(String name);
    
    @Query(value = "select * from m_bank where is_delete = false", nativeQuery = true)
    List<Bank> findAllActive();

}
