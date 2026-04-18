package com.crm.inventory.repository;

import com.crm.inventory.model.Product;
import com.crm.persistence.repository.CrudRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository implements CrudRepository<Product> {
    private final Map<String, Product> repository = new HashMap<>();

    @Override
    public void save(String id, Product entity) {
        repository.put(id, entity);
    }

    @Override
    public Product findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, Product entity) {
        repository.put(id, entity);
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
    }

    public Collection<Product> findAll() {
        return repository.values();
    }
}
