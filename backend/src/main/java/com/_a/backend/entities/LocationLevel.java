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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_location_level")
@AllArgsConstructor
@NoArgsConstructor
public class LocationLevel extends BaseEntity {
    public LocationLevel(String name, String abbrevation) {
        this.name = name;
        this.abbreviation = abbrevation;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "abbreviation", length = 50)
    private String abbreviation;

    @OneToMany(mappedBy = "locationLevel", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Location> locations;
}
