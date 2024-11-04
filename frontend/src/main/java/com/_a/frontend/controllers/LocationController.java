package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/location")
public class LocationController {

    @GetMapping("")
    public String index() {
        return "pages/location/index";
    }

    @GetMapping("/addForm")
    public ModelAndView form() {
        ModelAndView view = new ModelAndView("pages/location/addForm");
        return view;
    }

    @GetMapping("/editForm")
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("pages/location/editForm");
        return view;
    }

    @GetMapping("/deleteModal")
    public ModelAndView delete() {
        ModelAndView view = new ModelAndView("pages/location/deleteModal");
        return view;
    }

}
