package com.github.hadi_awan.smarthome.smart_home_management.service;

import com.github.hadi_awan.smarthome.smart_home_management.model.Zone;
import com.github.hadi_awan.smarthome.smart_home_management.repository.ZoneRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZoneService {

    @Autowired
    ZoneRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    public Zone findById(Long id) {
        return repository.findById(id).get();
    }

    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    public List<Zone> findByHome(Long homeID) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Zone> query = cb.createQuery(Zone.class);
        Root<Zone> zone = query.from(Zone.class);

        Path<String> idPath = zone.get("home");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(idPath, homeID));
        query.select(zone).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }

    public Zone save(Zone zone) {
        return repository.save(zone);
    }

}
