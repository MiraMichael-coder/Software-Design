package com.crm.inventory.repository;

import com.crm.inventory.model.InventoryRecord;
import com.crm.persistence.repository.CrudRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InventoryRecordRepository implements CrudRepository<InventoryRecord> {
    private final Map<String, InventoryRecord> repository = new HashMap<>();

    @Override
    public void save(String id, InventoryRecord entity) {
        repository.put(id, entity);
    }

    @Override
    public InventoryRecord findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, InventoryRecord entity) {
        repository.put(id, entity);
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
    }

    public Collection<InventoryRecord> findAll() {
        return repository.values();
    }
}
