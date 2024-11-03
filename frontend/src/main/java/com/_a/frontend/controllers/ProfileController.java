package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
  @GetMapping("")
  public String index() {
    return "pages/profile/index";
  }

}
