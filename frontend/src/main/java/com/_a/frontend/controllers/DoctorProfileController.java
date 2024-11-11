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
  
    @GetMapping("/addFormSpecialization")
    public ModelAndView form() {
        ModelAndView view = new ModelAndView("pages/doctor-profile/addFormSpecialization");
        return view;
    }
    
    @GetMapping("/editFormSpecialization")
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("pages/doctor-profile/editFormSpecialization");
        return view;
    }

    @GetMapping("/addFormTreatment")
    public ModelAndView formTreatment() {
        ModelAndView view = new ModelAndView("pages/doctor-profile/addFormTreatment");
        return view;
    }

    @GetMapping("/deleteFormTreatment")
    public ModelAndView deleteformTreatment() {
        ModelAndView view = new ModelAndView("pages/doctor-profile/deleteFormTreatment");
        return view;
    }
    
    
}