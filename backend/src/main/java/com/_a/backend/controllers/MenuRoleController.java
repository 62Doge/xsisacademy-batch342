package com._a.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.services.impl.MenuRoleServiceImpl;

@RestController
@RequestMapping("/api/transaction/menu-role")
@CrossOrigin("*")
public class MenuRoleController {
    @Autowired
    private MenuRoleServiceImpl menuRoleService; 

    @PostMapping("/save-menu-access/{roleId}")
    public ResponseEntity<?> saveMenuAccess(@PathVariable("roleId") Long roleId, @RequestBody List<Long> selectedMenuIds) {
        // Process the selectedMenuIds (e.g., update MenuRole records)
        menuRoleService.updateMenuAccessForRole(roleId, selectedMenuIds);

        return ResponseEntity.ok("Access updated successfully");
    }

}
