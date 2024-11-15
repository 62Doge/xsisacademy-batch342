package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/profile")
public class ProfileController {
  @GetMapping("")
  public String index() {
    return "pages/profile/index";
  }


  @GetMapping("/editPersonalDataForm")
  public ModelAndView editPersonalDataForm() {
    ModelAndView view = new ModelAndView("pages/profile/editPersonalDataForm");
    return view;
  }

  @GetMapping("/editEmailForm")
  public ModelAndView editEmailForm() {
    ModelAndView view = new ModelAndView("pages/profile/editEmailForm");
    return view;
  }

  @GetMapping("/editPasswordForm")
  public ModelAndView editPasswordForm() {
    ModelAndView view = new ModelAndView("pages/profile/editPasswordForm");
    return view;
  }

  @GetMapping("/newPasswordForm")
  public ModelAndView newPasswordForm() {
    ModelAndView view = new ModelAndView("pages/profile/newPasswordForm");
    return view;
  }

}
