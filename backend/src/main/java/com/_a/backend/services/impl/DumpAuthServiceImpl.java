package com._a.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.entities.User;
import com._a.backend.repositories.UserRepository;
import com._a.backend.services.AuthService;

@Service
public class DumpAuthServiceImpl implements AuthService {
  @Autowired
  UserRepository userRepository;

  public User getDetails() {
    return userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Unathenticated"));
  }
}
