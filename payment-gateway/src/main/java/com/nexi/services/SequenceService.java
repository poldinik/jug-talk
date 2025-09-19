package com.nexi.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SequenceService {

    private final EntityManager entityManager;

    @Inject
    public SequenceService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public String getNextValueFromSequence() {
        return entityManager.createNativeQuery("SELECT NEXT VALUE FOR HIBERNATE_SEQUENCE").getResultList().get(0).toString();
    }
}
