package com._a.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "m_payment_method")
@EqualsAndHashCode(callSuper = true)
public class PaymentMethod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<MedicalItemPurchase> medicalItemPurchases;
}
