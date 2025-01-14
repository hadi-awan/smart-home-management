package com.github.hadi_awan.smarthome.smart_home_management.controller;

import com.github.hadi_awan.smarthome.smart_home_management.exception.UnauthorizedActionException;
import com.github.hadi_awan.smarthome.smart_home_management.model.Appliance;
import com.github.hadi_awan.smarthome.smart_home_management.model.Home;
import com.github.hadi_awan.smarthome.smart_home_management.model.User;
import com.github.hadi_awan.smarthome.smart_home_management.model.Zone;
import com.github.hadi_awan.smarthome.smart_home_management.service.ApplianceService;
import com.github.hadi_awan.smarthome.smart_home_management.service.HomeService;
import com.github.hadi_awan.smarthome.smart_home_management.service.UserService;
import com.github.hadi_awan.smarthome.smart_home_management.service.ZoneService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8081") // Adjust port if needed
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    HomeService homeService;

    @Autowired
    ZoneService zoneService;

    @Autowired
    ApplianceService applianceService;

    private Map<String, String> response;

    // DTO for user signup request
    public static class UserSignupRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        private Boolean isParent;
        private Boolean isChild;
        private Boolean isGuest;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public Boolean getIsParent() { return isParent; }
        public void setIsParent(Boolean parent) { isParent = parent; }
        public Boolean getIsChild() { return isChild; }
        public void setIsChild(Boolean child) { isChild = child; }
        public Boolean getIsGuest() { return isGuest; }
        public void setIsGuest(Boolean guest) { isGuest = guest; }
    }

    public UserController() {
        this.response = new HashMap<String, String>();
        this.response.put("success", "true");
    }

    @GetMapping("/users")
    public ResponseEntity<?> index() {
        try {
            List<User> users = userService.findAll();
            logger.info("Found {} users", users.size()); // Add logging
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error fetching users", e); // Add logging
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to fetch users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PostMapping("/user/update")
    public Map<String, String> update(@RequestParam(value = "id") Long id,
                                      @RequestParam(value = "home_id", required = false) Long homeId,
                                      @RequestParam(value = "zone_id", required = false) Long zoneId,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "password", required = false) String password) {

        User user = userService.findById(id);
        setUserHome(homeId, user);
        setUserZone(zoneId, user);
        getAppliancesFromZone(user);

        if (user.getHome().getSecurityLevel() != null &&
                user.getHome().getSecurityLevel().equals(Home.SECURITY_ARMED) &&
                user.getZone().getId() != 0) {
            this.response.put("success", "false");
            this.response.put("message", "Alarm has been triggered. Please leave the home and disable the alarm.");
            return this.response;
        }

        if (name != null) {
            user.setName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (password != null) {
            user.setPassword(passwordEncoder().encode(password));
        }

        userService.save(user);
        return this.response;
    }

    public void setUserHome(Long homeId, User user) {
        if (homeId != null && homeService.exists(homeId)) {
            Home home = homeService.findById(homeId);
            user.setHome(home);
        } else if (homeId != null) {
            this.response.put("home", "Home supplied does not exist");
        }
    }

    public void setUserZone(Long zoneId, User user) {
        if (zoneId != null && zoneService.exists(zoneId)) {
            Zone zone = zoneService.findById(zoneId);
            user.setZone(zone);
        } else if (zoneId != null && zoneId == 0) {
            user.setZone(null);
        } else if (zoneId != null) {
            this.response.put("zone", "Zone supplied does not exist");
        }
    }

    public void getAppliancesFromZone(User user) {
        if (user.getHome().getAutoMode() == 1) {
            for (Appliance appliance : user.getZone().getAppliances()) {
                if (appliance != null && appliance.getType().equals("light") && appliance.getState() != 1) {
                    appliance.setState(1);
                    applianceService.save(appliance);
                }
            }
        }
    }

    @GetMapping("/user")
    public User show(@RequestParam(value = "id") Long id) {
        return userService.findById(id);
    }

    @GetMapping("/user/login")
    public User getUser(@RequestParam(value = "email") String email,
                        @RequestParam(value = "password") String password) {
        return userService.findUserByCredentials(email, password);
    }

    @GetMapping("/user/current")
    public User currentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass() == String.class) {
            return null;
        } else {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
    }

    @PostMapping(value = "/user/store", consumes = "application/json")
    public Map<String, String> store(@Valid @RequestBody UserSignupRequest request) {
        try {
            String password = this.passwordEncoder().encode(request.getPassword());
            String role;

            if (Boolean.TRUE.equals(request.getIsParent())) {
                role = User.ROLE_PARENT;
            } else if (Boolean.TRUE.equals(request.getIsChild())) {
                role = User.ROLE_CHILD;
            } else {
                role = User.ROLE_USER;
            }

            User user = new User(request.getName(), request.getEmail(), password, role);

            if (userService.save(user) != null) {
                this.response.put("success", "true");
                return this.response;
            }

            this.response.put("success", "false");
            this.response.put("message", "Failed to save user");
            return this.response;

        } catch (Exception e) {
            this.response.put("success", "false");
            this.response.put("message", e.getMessage());
            return this.response;
        }
    }

    @PostMapping("/users/destroy")
    public ResponseEntity<Map<String, String>> destroy(@RequestParam(value = "id") Long id) {
        try {
            // Temporarily remove authorization check for testing
            userService.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "User successfully deleted");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            Map<String, String> error = new HashMap<>();
            error.put("success", "false");
            error.put("message", "Error deleting user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}