package com._a.backend.controllers;

import com._a.backend.services.impl.LocationLevelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location-level")
public class LocationLevelController {
    @Autowired
    private LocationLevelServiceImpl locationLevelService;


}
