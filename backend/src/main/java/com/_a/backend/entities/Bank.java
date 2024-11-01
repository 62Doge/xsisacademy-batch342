package com._a.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "m_bank")
public class Bank extends BaseEntity {

    public Bank() {

    }

    public Bank(String name, String vaCode) {
        this.name = name;
        this.vaCode = vaCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100, nullable = true)
    private String name;

    @Column(name = "va_code", length = 10, nullable = true)
    private String vaCode;

}
