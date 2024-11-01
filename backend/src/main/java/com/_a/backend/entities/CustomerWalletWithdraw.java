package com._a.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_customer_wallet_withdraw")
public class CustomerWalletWithdraw extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "customer_id", insertable = false, updatable = false)
  private Customer customer;

  @Column(name = "customer_id", nullable = false)
  private Long customerId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "wallet_default_nominal_id", insertable = false, updatable = false)
  private WalletDefaultNominal walletDefaultNominal;

  @Column(name = "wallet_default_nominal_id")
  private Long walletDefaultNominalId;

  @Column(nullable = false)
  private int amount;

  @Column(length = 50)
  private String bankName;

  @Column(length = 50)
  private String accountNumber;

  @Column(length = 255)
  private String accountName;

  @Column(nullable = false)
  private int otp;
}
