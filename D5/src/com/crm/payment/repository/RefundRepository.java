package com.crm.payment.repository;

import com.crm.payment.model.Refund;
import com.crm.persistence.repository.CrudRepository;

import java.util.HashMap;
import java.util.Map;

public class RefundRepository implements CrudRepository<Refund> {
    private final Map<String, Refund> repository = new HashMap<>();

    @Override
    public void save(String id, Refund entity) {
        repository.put(id, entity);
        System.out.println("Refund saved in database");
    }

    @Override
    public Refund findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, Refund entity) {
        repository.put(id, entity);
        System.out.println("Refund updated");
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
        System.out.println("Refund removed");
    }
}
