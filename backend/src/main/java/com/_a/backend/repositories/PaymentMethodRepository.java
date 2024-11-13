package com._a.backend.repositories;

import com._a.backend.entities.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Boolean existsByName(String name);
    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalse(String name);
    Boolean existsByNameContainingIgnoreCaseAndIsDeleteFalseAndIdNot(String name, long id);
    Page<PaymentMethod> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);
    Page<PaymentMethod> findAllByIsDeleteFalse(Pageable pageable);
}
