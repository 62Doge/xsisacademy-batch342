package com._a.backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "t_doctor_office_treatment_price")
public class DoctorOfficeTreatmentPrice extends BaseEntity {
    
    public DoctorOfficeTreatmentPrice(Long doctorOfficeTreatmentId, Double price, Double priceStartFrom, Double priceUntilFrom) {
        this.doctorOfficeTreatmentId = doctorOfficeTreatmentId;
        this.price = price;
        this.priceStartFrom = priceStartFrom;
        this.priceUntilFrom = priceUntilFrom;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "doctor_office_treatment_id", insertable = false, updatable = false)
    @JsonManagedReference
    private DoctorOfficeTreatment doctorOfficeTreatment;

    @Column(name = "doctor_office_treatment_id")
    private Long doctorOfficeTreatmentId;

    @Column(name = "price")
    private Double price;

    @Column(name = "price_start_from")
    private Double priceStartFrom;

    @Column(name = "price_until_from")
    private Double priceUntilFrom;
    
}
