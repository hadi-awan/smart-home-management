package com.github.hadi_awan.smarthome.smart_home_management.repository;

import com.github.hadi_awan.smarthome.smart_home_management.model.Home;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends CrudRepository<Home, Long> {
}
