package com._a.backend.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_location")
@AllArgsConstructor
@NoArgsConstructor
public class Location extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    // @JsonBackReference
    private Location parent;
    
    @Column(name = "parent_id")
    private Long parentId;
    
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "location_level_id", insertable = false, updatable = false)
    @JsonBackReference
    private LocationLevel locationLevel;
    
    @Column(name = "location_level_id")
    private Long locationLevelId;
    
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<MedicalFacility> medicalFacilities;
    
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<BiodataAddress> biodataAddresses;
    
    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Location> childs;
}
