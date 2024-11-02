package com._a.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com._a.backend.dtos.responses.CustomerWalletSummaryResponseDto;
import com._a.backend.entities.CustomerWallet;

public interface CustomerWalletRepository extends JpaRepository<CustomerWallet, Long> {

  @Query("""
      select cw
      from CustomerWallet cw
      join cw.customer c
      join c.biodata b
      join User u on b.id=u.biodataId
      where u.id=?1
      and cw.isDelete=false
      """)
  Optional<CustomerWalletSummaryResponseDto> findByUserId(Long userId);

  @Query("""
      select cw.balance
      from CustomerWallet cw
      join cw.customer c
      join c.biodata b
      join User u on b.id=u.biodataId
      where u.id=?1
      and cw.isDelete=false
      """)
  Optional<Double> findBalanceByUserId(Long userId);
}
