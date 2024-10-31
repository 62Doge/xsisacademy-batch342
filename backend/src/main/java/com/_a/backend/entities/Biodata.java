package com._a.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "m_biodata")
public class Biodata {
  
  public Biodata(String fullname, String mobile_phone,byte[] image, String image_path){
    this.fullname = fullname;
    this.mobilePhone = mobile_phone;
    this.image = image;
    this.imagePath = image_path;
  }
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "fullname", length = 255)
  private String fullname;

  @Column(name = "mobile_phone", length = 15)
  private String mobilePhone;

  @Lob
  @Column(name = "image")
  private byte[] image;

  @Column(name = "image_path", length = 255)
  private String imagePath;
}
