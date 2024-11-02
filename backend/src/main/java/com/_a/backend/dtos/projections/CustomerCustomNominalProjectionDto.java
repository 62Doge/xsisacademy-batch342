package com._a.backend.dtos.projections;

public interface CustomerCustomNominalProjectionDto {
  Long getId();

  int getNominal();

  default String getType() {
    return "custom";
  }
}
