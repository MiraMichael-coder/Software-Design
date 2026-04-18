package com.crm.inventory.repository;

import com.crm.inventory.model.PurchaseOrder;
import com.crm.persistence.repository.CrudRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PurchaseOrderRepository implements CrudRepository<PurchaseOrder> {
    private final Map<String, PurchaseOrder> repository = new HashMap<>();

    @Override
    public void save(String id, PurchaseOrder entity) {
        repository.put(id, entity);
    }

    @Override
    public PurchaseOrder findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, PurchaseOrder entity) {
        repository.put(id, entity);
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
    }

    public Collection<PurchaseOrder> findAll() {
        return repository.values();
    }
}
