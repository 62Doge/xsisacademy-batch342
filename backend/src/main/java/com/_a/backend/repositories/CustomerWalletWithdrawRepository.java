package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.CustomerWalletWithdraw;

public interface CustomerWalletWithdrawRepository extends JpaRepository<CustomerWalletWithdraw, Long> {

}
