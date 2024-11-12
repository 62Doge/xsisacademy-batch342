package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/dump-find-doctor")
public class DumpFindDoctor {
    
    @GetMapping("")
    public String index() {
        return "pages/dump-find-doctor/index";
    }

}
