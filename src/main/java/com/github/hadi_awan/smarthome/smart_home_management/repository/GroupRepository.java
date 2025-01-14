package com.github.hadi_awan.smarthome.smart_home_management.repository;

import com.github.hadi_awan.smarthome.smart_home_management.model.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
}
