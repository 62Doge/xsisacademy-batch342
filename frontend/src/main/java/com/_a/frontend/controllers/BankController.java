package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/bank")
public class BankController {
    
    @GetMapping("")
    public String index() {
        return "pages/bank/index";
    }

    @GetMapping("/addForm")
    public ModelAndView form() {
        ModelAndView view = new ModelAndView("pages/bank/addForm");
        return view;
    }

    @GetMapping("/editForm")
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("pages/bank/editForm");
        return view;
    }

    @GetMapping("/deleteModal")
    public ModelAndView delete() {
        ModelAndView view = new ModelAndView("pages/bank/deleteModal");
        return view;
    }

}
