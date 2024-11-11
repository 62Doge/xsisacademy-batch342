package com._a.backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "m_biodata_attachment")
public class BiodataAttachment extends BaseEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "file_name", length = 50)
  private String fileName;

  @Column(name = "file_path", length = 100)
  private String filePath;

  @Column(name = "file_size")
  private Long fileSize;

  @Lob
  @Column(name = "file")
  private byte[] file;

  @ManyToOne
  @JoinColumn(name = "biodata_id", insertable = false, updatable = false)
  @JsonManagedReference
  private Biodata biodata;

  @Column(name = "biodata_id")
  private Long biodataId;
}
