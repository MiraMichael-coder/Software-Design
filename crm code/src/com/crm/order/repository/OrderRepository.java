package com.crm.order.repository;

<<<<<<< Updated upstream
import com.crm.order.model.Order;
import com.crm.persistence.repository.CrudRepository;

public class OrderRepository implements CrudRepository<Order> {

    @Override
    public void save(Order entity) { }

    @Override
    public Order findById(String id) { return null; }

    @Override
    public void update(Order entity) { }

    @Override
    public void delete(String id) { }
=======
import com.crm.alert.model.SystemAlert;
import com.crm.order.model.Order;
import com.crm.persistence.repository.CrudRepository;

import java.util.HashMap;
import java.util.Map;

public class OrderRepository implements CrudRepository<Order> {
    private final Map<String, Order> repository = new HashMap<>();

    @Override
    public void save(String id,Order entity) {
        repository.put(id, entity);
        System.out.println("Order Saved to database");

    }

    @Override
    public Order findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, Order entity) {
        if (repository.containsKey(id)) {
            repository.put(id, entity);
            System.out.println("Order updated to database");
        }
        else  {
            System.out.println("Order not found");
        }
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
    System.out.println("Order deleted from database");
    }
>>>>>>> Stashed changes
}
