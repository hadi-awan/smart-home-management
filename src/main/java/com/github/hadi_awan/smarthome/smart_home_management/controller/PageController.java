package com.github.hadi_awan.smarthome.smart_home_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/signup")
    public String signup(){
        return "signup";
    }

    @RequestMapping("/edit/profile")
    public String editProfile(){
        return "editProfile";
    }


    @RequestMapping("/edit/home")
    public String editHome(){
        return "editHome";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "home";  // You'll need to create home.html in templates folder
    }
}
