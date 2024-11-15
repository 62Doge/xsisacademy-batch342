package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @GetMapping("/emailForm")
    public ModelAndView emailForm() {
        ModelAndView modelAndView = new ModelAndView("/pages/registration/registForm");
        return modelAndView;
    }


}
