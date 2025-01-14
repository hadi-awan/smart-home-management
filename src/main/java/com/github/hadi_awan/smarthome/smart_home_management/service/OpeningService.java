package com.github.hadi_awan.smarthome.smart_home_management.service;

import com.github.hadi_awan.smarthome.smart_home_management.model.Opening;
import com.github.hadi_awan.smarthome.smart_home_management.repository.OpeningRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpeningService {

    @Autowired
    OpeningRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    public Opening findById(Long id) {
        return repository.findById(id).get();
    }

    public Opening save(Opening opening) {
        return repository.save(opening);
    }

    public List<Opening> findByZoneAndType(Long zoneId, String type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Opening> query = cb.createQuery(Opening.class);
        Root<Opening> opening = query.from(Opening.class);

        Path<String> idPath = opening.get("zone");
        Path<String> typePath = opening.get("type");
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(idPath, zoneId));
        predicates.add(cb.equal(typePath, type));
        query.select(opening).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }

    public List<Opening> findByZone(Long zoneId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Opening> query = cb.createQuery(Opening.class);
        Root<Opening> opening = query.from(Opening.class);

        Path<String> idPath = opening.get("zone");
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(idPath, zoneId));
        query.select(opening).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }
}
