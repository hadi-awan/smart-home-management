package com.github.hadi_awan.smarthome.smart_home_management.service;

import com.github.hadi_awan.smarthome.smart_home_management.model.Group;
import com.github.hadi_awan.smarthome.smart_home_management.model.Zone;
import com.github.hadi_awan.smarthome.smart_home_management.repository.GroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    GroupRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    public Group findById(Long id) {
        return repository.findById(id).get();
    }

    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    public Group save(Group group) {
        return repository.save(group);
    }

    public List<Zone> findByGroup(Long groupId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Zone> query = cb.createQuery(Zone.class);
        Root<Zone> zone = query.from(Zone.class);

        Path<String> idPath = zone.get("group");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(idPath, groupId));
        query.select(zone).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }
}
