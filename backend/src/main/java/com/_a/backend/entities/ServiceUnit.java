package com._a.backend.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode (callSuper = true)
@NoArgsConstructor
@Table(name = "m_service_unit")
public class ServiceUnit extends BaseEntity {
  
  public ServiceUnit(String name) {
    this.name = name;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", length = 50)
  private String name;

  @OneToMany(mappedBy = "serviceUnit", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<DoctorOffice> doctorOffices;
}
