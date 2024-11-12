package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/doctor-details")
public class DoctorDetailsController {
    
    @GetMapping("")
    public String index() {
        return "pages/doctor-details/index";
    }
    
}
