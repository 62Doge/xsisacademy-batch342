package com._a.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.CustomerMember;

@Repository
public interface CustomerMemberRepository extends JpaRepository<CustomerMember, Long> {
    
    Page<CustomerMember> findAllByIsDeleteFalse(Pageable page);

}
