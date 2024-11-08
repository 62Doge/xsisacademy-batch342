package com._a.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/role")
public class RoleController {

    @GetMapping({ "", "/" })
    public String index() {
        return "pages/role/index";
    }

    @GetMapping({ "/addForm", "/addForm/" })
    public String form() {
        return "pages/role/addForm";
    }

    @GetMapping({ "/editForm", "/editForm/" })
    public String edit() {
        return "pages/role/editForm";
    }

    @GetMapping({ "/deleteModal", "/deleteModal/" })
    public String delete() {
        return "pages/role/deleteModal";
    }

}
