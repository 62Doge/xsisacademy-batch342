package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._a.backend.dtos.projections.WalletDefaultNominalProjectionDto;
import com._a.backend.entities.WalletDefaultNominal;

@Repository
public interface WalletDefaultNominalRepository extends JpaRepository<WalletDefaultNominal, Long> {
  @Query("""
      select wn
      from WalletDefaultNominal wn
      where wn.isDelete=false
      and cast(wn.nominal as double) <= ?1
      """)
  List<WalletDefaultNominalProjectionDto> findAllByNominalLessThanEqual(Double balance);
}
