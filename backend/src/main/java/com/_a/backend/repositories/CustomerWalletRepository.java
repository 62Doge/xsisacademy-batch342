package com._a.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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

  @Query("""
      select cw.pin
      from CustomerWallet cw
      join cw.customer c
      join c.biodata b
      join User u on b.id=u.biodataId
      where u.id=?1
      and cw.isDelete=false
      """)
  Optional<String> findPinByUserId(Long userId);

  @Transactional
  @Modifying
  @Query("""
      update CustomerWallet cw
      set cw.pinAttempt = cw.pinAttempt+1
      where cw.isDelete = false
      and cw.id = ?1
      """)
  int incrementPinAttemptByUserId(Long walletId);

  @Transactional
  @Modifying
  @Query("""
      update CustomerWallet cw
      set cw.isBlocked = true
      where cw.isDelete = false
      and cw.id = ?1
      """)
  void blockWallet(Long walletId);

  @Transactional
  @Modifying
  @Query("""
      update CustomerWallet cw
      set cw.pinAttempt = 0
      where cw.isDelete = false
      and cw.id = ?1
      """)
  void resetPinAttempt(Long walletId);
}
