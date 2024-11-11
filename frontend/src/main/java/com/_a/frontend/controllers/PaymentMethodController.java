package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/payment-method")
public class PaymentMethodController {

    @GetMapping("")
    public String index() {
        return "pages/payment-method/index";
    }

    @GetMapping("/addForm")
    public ModelAndView form() {
        ModelAndView view = new ModelAndView("pages/payment-method/addForm");
        return view;
    }

    @GetMapping("/editForm")
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("pages/payment-method/editForm");
        return view;
    }

    @GetMapping("/deleteModal")
    public ModelAndView delete() {
        ModelAndView view = new ModelAndView("pages/payment-method/deleteModal");
        return view;
    }
}
