package com._a.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com._a.backend.dtos.projections.CustomerCustomNominalProjectionDto;
import com._a.backend.entities.CustomerCustomNominal;

public interface CustomerCustomNominalRepository extends JpaRepository<CustomerCustomNominal, Long> {
  @Query("""
      select cc
      from CustomerCustomNominal cc
      join cc.customer c
      join c.biodata b
      join User u on u.biodataId=b.id
      where cc.isDelete=false
      and cc.nominal <= cast(?1 as integer)
      and u.id=?2
      """)
  Optional<List<CustomerCustomNominalProjectionDto>> findAllByNominalLessThanEqual(Double balance, Long userId);
}
