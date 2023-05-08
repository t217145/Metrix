package com.metrix.webportal.controllers;

/* 1. Please implement this class
 * refer here https://github.com/t217145/COMPS368-Code/tree/main/u7/jdbcauth.web
*/
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String index(ModelMap m){
        return "login";
    }
}