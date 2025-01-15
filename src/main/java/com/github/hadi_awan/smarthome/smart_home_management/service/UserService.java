
package com.github.hadi_awan.smarthome.smart_home_management.service;

import com.github.hadi_awan.smarthome.smart_home_management.model.User;
import com.github.hadi_awan.smarthome.smart_home_management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository repository;

    public List<User> findAll() {
        var iterator = repository.findAll();
        var users = new ArrayList<User>();
        iterator.forEach(e -> users.add(e));

        return users;
    }

    public User findById(Long id) {
        return repository.findById(id).get();
    }

    public User findByEmail(String email) {
        User user = repository.findByEmail(email);
        if (user != null) {
            logger.info("Found user with email: {}", email);
            logger.info("Password present: {}", user.getPassword() != null);
            logger.info("Password value: {}", user.getPassword());
        }
        return user;
    }

    public User save(User user) {
        return repository.save(user);
    }

    // save list of users
    public List<User> saveUsers(List<User> users) {
        return (List<User>) repository.saveAll(users);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public User findUserByCredentials(String email, String password) {
        logger.info("Attempting to find user with email: {}", email);
        User potentialUser = repository.findByEmail(email);

        if (potentialUser != null) {
            logger.info("Found user with email: {}", email);
            logger.info("Stored password hash: {}", potentialUser.getPassword());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean passwordMatches = encoder.matches(password, potentialUser.getPassword());
            logger.info("Password match result: {}", passwordMatches);

            if (passwordMatches) {
                return potentialUser;
            }
        } else {
            logger.info("No user found with email: {}", email);
        }

        return null;
    }
}
