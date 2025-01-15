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
import java.util.ArrayList;

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
    public ResponseEntity<?> getUser(@RequestParam(value = "email") String email,
                                     @RequestParam(value = "password") String password) {
        logger.info("Login attempt for: {}", email);
        User user = userService.findUserByCredentials(email, password);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("success", "false");
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/check-users")
    public ResponseEntity<?> checkUsers() {
        try {
            List<User> users = userService.findAll();
            List<Map<String, String>> userInfo = new ArrayList<>();

            for (User user : users) {
                Map<String, String> info = new HashMap<>();
                info.put("id", String.valueOf(user.getId()));
                info.put("email", user.getEmail());
                info.put("name", user.getName());
                info.put("hasPassword", user.getPassword() != null ? "yes" : "no");
                info.put("passwordLength", user.getPassword() != null ? String.valueOf(user.getPassword().length()) : "0");
                userInfo.add(info);
            }

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            logger.error("Error checking users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking users: " + e.getMessage());
        }
    }

    @GetMapping("/debug/user")
    public ResponseEntity<?> debugUser(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user != null) {
            Map<String, Object> info = new HashMap<>();
            info.put("id", user.getId());
            info.put("email", user.getEmail());
            info.put("name", user.getName());
            info.put("passwordHash", user.getPassword());
            info.put("role", user.getRole());
            return ResponseEntity.ok(info);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/current")
    public User currentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass() == String.class) {
            return null;
        } else {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
    }

    @PostMapping(value = "/user/store")
    public ResponseEntity<?> store(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "email") String email,
                                   @RequestParam(value = "password") String password,
                                   @RequestParam(value = "isParent") Boolean isParent,
                                   @RequestParam(value = "isChild") Boolean isChild,
                                   @RequestParam(value = "isGuest") Boolean isGuest) {
        try {
            String encodedPassword = passwordEncoder().encode(password);
            logger.info("Storing user with email: {}", email);
            logger.info("Encoded password hash: {}", encodedPassword);

            String role;
            if (isParent) {
                role = User.ROLE_PARENT;
            } else if (isChild) {
                role = User.ROLE_CHILD;
            } else {
                role = User.ROLE_USER;
            }

            User user = new User(name, email, encodedPassword, role); // Note: passing encoded password
            user.setUsername(email); // Ensure username is set
            user = userService.save(user);

            // Log saved user details
            logger.info("Saved user ID: {}", user.getId());
            logger.info("Saved password present: {}", user.getPassword() != null);
            logger.info("Saved password hash: {}", user.getPassword());

            Map<String, String> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "User created successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating user", e);
            Map<String, String> response = new HashMap<>();
            response.put("success", "false");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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

    @PostMapping("/user/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        try {
            User user = userService.findByEmail(email);
            if (user != null) {
                user.setPassword(passwordEncoder().encode(newPassword));
                userService.save(user);

                Map<String, String> response = new HashMap<>();
                response.put("success", "true");
                response.put("message", "Password updated successfully");
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("success", "false");
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            logger.error("Error resetting password", e);
            Map<String, String> response = new HashMap<>();
            response.put("success", "false");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @GetMapping("/debug/user-details")
    public ResponseEntity<?> debugUserDetails(@RequestParam String email) {
        try {
            User user = userService.findByEmail(email);
            if (user != null) {
                Map<String, Object> details = new HashMap<>();
                details.put("id", user.getId());
                details.put("email", user.getEmail());
                details.put("hasPassword", user.getPassword() != null);
                details.put("passwordHash", user.getPassword()); // For debugging only!
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error getting user details", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}