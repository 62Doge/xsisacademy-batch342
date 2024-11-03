package com._a.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  @Query(value = """
      select c
      from Customer c
      join c.biodata b
      join User u on u.biodataId = b.id
      where c.isDelete=false
      and u.id = ?1
      """)
  Optional<Customer> findByUserId(Long userId);
}
