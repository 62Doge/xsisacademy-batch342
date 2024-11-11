package com._a.backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "m_admin")
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "biodata_id", insertable = false, updatable = false)
    @JsonManagedReference
    private Biodata biodata;

    @Column(name = "biodata_id")
    private Long biodataId;

    @Column(name = "code")
    private String code;

}
