package com._a.frontend.controllers;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/set-menu-access")
public class SetMenuAccessController {
  
  @GetMapping("")
  public String index() {
    return "pages/set-menu-access/index";
  }

  @GetMapping("/updateForm")
    public ModelAndView form() {
        ModelAndView view = new ModelAndView("pages/set-menu-access/updateForm");
        return view;
    }

}
