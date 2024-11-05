package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/role")
public class RoleController {

    @GetMapping("")
    public String index() {
        return "pages/role-access/index";
    }

    @GetMapping("/addForm")
    public ModelAndView form() {
        ModelAndView view = new ModelAndView("pages/role-access/addForm");
        return view;
    }

    @GetMapping("/editForm")
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("pages/role-access/editForm");
        return view;
    }

    @GetMapping("/deleteModal")
    public ModelAndView delete() {
        ModelAndView view = new ModelAndView("pages/role-access/deleteModal");
        return view;
    }

}
