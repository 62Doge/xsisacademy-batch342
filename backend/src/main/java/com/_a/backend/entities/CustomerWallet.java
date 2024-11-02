package com._a.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "t_customer_wallet")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerWallet extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "customer_id", insertable = false, updatable = false)
  private Customer customer;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(length = 6)
  private String pin;

  private Double balance;

  @Column(length = 50)
  private String barcode;

  private Double points;

  @Column(length = 1)
  private int pinAttempt;

  private LocalDateTime blockEnds;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private boolean isBlocked = false;
}
