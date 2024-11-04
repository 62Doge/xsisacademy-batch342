package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DumpLandingPage {
  @GetMapping({ "", "/" })
  public String index() {
    return "pages/landing/index";
  }

}
