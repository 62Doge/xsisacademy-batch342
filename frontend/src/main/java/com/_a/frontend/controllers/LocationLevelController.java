package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/location-level")
public class LocationLevelController {

    @GetMapping("")
    public String index() {
        return "pages/location-level/index";
    }

    @GetMapping("/addForm")
    public ModelAndView addForm() {
        ModelAndView modelAndView = new ModelAndView("pages/location-level/addForm");
        return modelAndView;
    }


}
