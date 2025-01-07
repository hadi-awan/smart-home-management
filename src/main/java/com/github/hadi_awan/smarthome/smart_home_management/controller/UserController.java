package com.github.hadi_awan.smarthome.smart_home_management.controller;

import com.github.hadi_awan.smarthome.smart_home_management.model.User;
import com.github.hadi_awan.smarthome.smart_home_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {

        return userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }
}
