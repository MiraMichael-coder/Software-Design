package com.crm.order.repository;

import com.crm.order.model.Order;
import com.crm.persistence.repository.CrudRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OrderRepository implements CrudRepository<Order> {
    private final Map<String, Order> repository = new HashMap<>();

    @Override
    public void save(String id, Order entity) {
        repository.put(id, entity);
    }

    @Override
    public Order findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, Order entity) {
        repository.put(id, entity);
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
    }

    public Collection<Order> findAll() {
        return repository.values();
    }
}
