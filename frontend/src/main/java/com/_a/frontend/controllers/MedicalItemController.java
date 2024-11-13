package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medical-item")
public class MedicalItemController {

    @GetMapping("")
    public String index() {
        return "pages/medical-item/index";
    }

}
