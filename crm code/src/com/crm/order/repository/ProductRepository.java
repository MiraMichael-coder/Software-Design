package com.crm.order.repository;

import com.crm.order.model.Product;
import com.crm.persistence.repository.CrudRepository;

<<<<<<< Updated upstream
public class ProductRepository implements CrudRepository<Product> {

    @Override
    public void save(Product entity) { }

    @Override
    public Product findById(String id) { return null; }

    @Override
    public void update(Product entity) { }

    @Override
    public void delete(String id) { }
=======
import java.util.HashMap;
import java.util.Map;

public class ProductRepository implements CrudRepository<Product> {
    private final Map<String, Product> repository = new HashMap<>();

    @Override
    public void save(String id, Product entity) {
        repository.put(id, entity);
        System.out.println("Product saved in database");
    }

    @Override
    public Product findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, Product entity) {
        repository.put(id, entity);
        System.out.println("Product updated");
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
        System.out.println("Product removed");
    }
>>>>>>> Stashed changes
}
