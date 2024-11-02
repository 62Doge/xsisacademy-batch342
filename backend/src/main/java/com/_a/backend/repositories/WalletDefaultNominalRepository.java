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
      join CustomerWalletWithdraw cww on cww.walletDefaultNominalId = wn.id
      join cww.customer c
      join c.biodata b
      join User u on u.biodataId = b.id
      where wn.isDelete=false
      and wn.nominal <= cast(?1 as integer)
      and u.id=?2
      """)
  List<WalletDefaultNominalProjectionDto> findAllByNominalLessThanEqual(Double balance, Long userId);
}
