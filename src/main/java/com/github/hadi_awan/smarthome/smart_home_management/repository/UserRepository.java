package com.github.hadi_awan.smarthome.smart_home_management.repository;

import com.github.hadi_awan.smarthome.smart_home_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
