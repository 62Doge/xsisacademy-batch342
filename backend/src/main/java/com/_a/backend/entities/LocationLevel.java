package com._a.backend.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

//    This is for m_location relation
//    @JsonManagedReference
//    private List<Location> locations;
}
