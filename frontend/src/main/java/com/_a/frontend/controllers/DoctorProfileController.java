package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/doctor-profile")
public class DoctorProfileController {

  @GetMapping("")
    public String index() {
        return "pages/doctor-profile/index";
    }
  
    @GetMapping("/addForm")
    public ModelAndView form() {
        ModelAndView view = new ModelAndView("pages/doctor-profile/addForm");
        return view;
    }
    
    @GetMapping("/editForm")
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("pages/doctor-profile/editForm");
        return view;
    }
    
    
  
}