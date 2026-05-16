package com.crm.order.repository;

import com.crm.order.model.Delivery;
import com.crm.persistence.repository.CrudRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DeliveryRepository implements CrudRepository<Delivery> {
    private final Map<String, Delivery> repository = new HashMap<>();

    @Override
    public void save(String id, Delivery entity) {
        repository.put(id, entity);
    }

    @Override
    public Delivery findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, Delivery entity) {
        repository.put(id, entity);
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
    }

    public Collection<Delivery> findAll() {
        return repository.values();
    }
}
