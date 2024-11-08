package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/blood-group")
public class BloodGroupController {
    
    @GetMapping("")
    public String index() {
        return "pages/blood-group/index";
    }

    @GetMapping("/addForm")
    public ModelAndView form() {
        ModelAndView view = new ModelAndView("pages/blood-group/addForm");
        return view;
    }

    @GetMapping("/editForm")
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("pages/blood-group/editForm");
        return view;
    }
    
    @GetMapping("/deleteModal")
    public ModelAndView delete() {
        ModelAndView view = new ModelAndView("pages/blood-group/deleteModal");
        return view;
    }
    
}
