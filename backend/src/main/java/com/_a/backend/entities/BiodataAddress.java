package com._a.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_biodata_address")
@AllArgsConstructor
@NoArgsConstructor
public class BiodataAddress extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "biodata_id", insertable = false, updatable = false)
    @JsonBackReference
    private Biodata biodata;
    
    @Column(name = "biodata_id")
    private Long biodataId;
    
    @Column(name = "label", length = 100)
    private String label;
    
    @Column(name = "recipient", length = 100)
    private String recipient;
    
    @Column(name = "recipient_phone_number", length = 15)
    private String recipientPhoneNumber;
    
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "location_id", insertable = false, updatable = false)
    @JsonBackReference
    private Location location;
    
    @Column(name = "location_id")
    private Long locationId;
}
