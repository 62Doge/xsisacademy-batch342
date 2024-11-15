package com._a.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com._a.backend.dtos.responses.BiodataResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.BiodataRepository;
import com._a.backend.services.impl.BiodataServiceImpl;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin("*")
public class BiodataController {

  @Autowired
  private BiodataServiceImpl biodataService;

  @Autowired
  private BiodataRepository biodataRepository;

  @PostMapping("/upload-image/{doctorId}")
  public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image, @RequestParam Long biodataId) {
      try {
          BiodataResponseDTO responseDTO = biodataService.uploadImage(biodataId, image);
          return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Image uploaded successfully", responseDTO));
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Image upload failed", null));
      }
  }
  
  
}
