package com.github.hadi_awan.smarthome.smart_home_management.repository;

import com.github.hadi_awan.smarthome.smart_home_management.model.Appliance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplianceRepository extends CrudRepository<Appliance, Long> {
}
