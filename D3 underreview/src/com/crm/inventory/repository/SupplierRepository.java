package com.crm.inventory.repository;

import com.crm.inventory.model.Supplier;
import com.crm.persistence.repository.CrudRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SupplierRepository implements CrudRepository<Supplier> {
    private final Map<String, Supplier> repository = new HashMap<>();

    @Override
    public void save(String id, Supplier entity) {
        repository.put(id, entity);
    }

    @Override
    public Supplier findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, Supplier entity) {
        repository.put(id, entity);
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
    }

    public Collection<Supplier> findAll() {
        return repository.values();
    }
}
