package com._a.backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_location_level")
@AllArgsConstructor
@NoArgsConstructor
public class LocationLevel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "abbreviation", length = 50)
    private String abbreviation;

    @OneToMany(mappedBy = "locationLevel", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Location> locations;
}
