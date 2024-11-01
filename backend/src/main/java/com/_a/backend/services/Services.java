package com._a.backend.services;


import java.util.List;
import java.util.Optional;

public interface Services<RequestDTO, ResponseDTO> {
    List<ResponseDTO> findAll();
    Optional<ResponseDTO> findById(Long id);
    ResponseDTO save(RequestDTO requestDTO);
    void deleteById(Long id);
}
