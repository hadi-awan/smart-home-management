package com.github.hadi_awan.smarthome.smart_home_management.service;

import com.github.hadi_awan.smarthome.smart_home_management.model.Home;
import com.github.hadi_awan.smarthome.smart_home_management.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    @Autowired
    HomeRepository repository;

    public Home findById(Long id) {
        return repository.findById(id).get();
    }

    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    public Home save(Home home) {
        return repository.save(home);
    }
}
