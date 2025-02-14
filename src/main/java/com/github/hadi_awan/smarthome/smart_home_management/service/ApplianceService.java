package com.github.hadi_awan.smarthome.smart_home_management.service;

import com.github.hadi_awan.smarthome.smart_home_management.model.Appliance;
import com.github.hadi_awan.smarthome.smart_home_management.repository.ApplianceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplianceService {

    @Autowired
    ApplianceRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    public Appliance findById(Long id) {
        return repository.findById(id).get();
    }

    public Appliance save(Appliance appliance) {
        return repository.save(appliance);
    }

    public List<Appliance> findByHome(Long homeId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appliance> query = cb.createQuery(Appliance.class);
        Root<Appliance> appliance = query.from(Appliance.class);

        Path<String> idPath = appliance.get("home");
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(idPath, homeId));
        query.select(appliance).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }

    public List<Appliance> findByZone(Long zoneId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appliance> query = cb.createQuery(Appliance.class);
        Root<Appliance> appliance = query.from(Appliance.class);

        Path<String> idPath = appliance.get("zone");
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(idPath, zoneId));
        query.select(appliance).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }

    public List<Appliance> findByZoneAndType(Long zoneId, String type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appliance> query = cb.createQuery(Appliance.class);
        Root<Appliance> appliance = query.from(Appliance.class);

        Path<String> idPath = appliance.get("zone");
        Path<String> typePath = appliance.get("type");
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(idPath, zoneId));
        predicates.add(cb.equal(typePath, type));
        query.select(appliance).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }
}
