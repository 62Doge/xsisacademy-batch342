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
@Table(name = "m_medical_item")
@AllArgsConstructor
@NoArgsConstructor
public class MedicalItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "medical_item_category_id")
    private Long medicalItemCategoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medical_item_category_id", insertable = false, updatable = false)
    private MedicalItemCategory medicalItemCategory;

    @Column(name = "composition", columnDefinition = "TEXT")
    private String composition;

    @Column(name = "medical_item_segmentation_id")
    private Long medicalItemSegmentationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medical_item_segmentation_id", insertable = false, updatable = false)
    private MedicalItemSegmentation medicalItemSegmentation;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "indication", columnDefinition = "TEXT")
    private String indication;

    @Column(name = "dosage", columnDefinition = "TEXT")
    private String dosage;

    @Column(name = "directions", columnDefinition = "TEXT")
    private String directions;

    @Column(name = "contraindication", columnDefinition = "TEXT")
    private String contraindication;

    @Column(name = "caution", columnDefinition = "TEXT")
    private String caution;

    @Column(name = "packaging", length = 50)
    private String packaging;

    @Column(name = "price_max")
    private Long priceMax;

    @Column(name = "price_min")
    private Long priceMin;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_path", length = 100)
    private String imagePath;

    // @OneToMany(mappedBy = "medicalItem", cascade = CascadeType.ALL)
    // @JsonBackReference
    // private List<MedicalItemPurchaseDetails> locations;

    @OneToMany(mappedBy = "medicalItem", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Prescription> prescriptions;
}
