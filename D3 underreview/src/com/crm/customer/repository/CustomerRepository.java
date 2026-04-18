package com.crm.customer.repository;

import com.crm.customer.model.Complaint;
import com.crm.customer.model.Customer;
import com.crm.persistence.repository.CrudRepository;

import java.util.HashMap;
import java.util.Map;

public class CustomerRepository implements CrudRepository<Customer> {

    private final Map<String, Customer> repository = new HashMap<>();

    @Override
    public void save(String Id,Customer entity) {
    repository.put(Id, entity);
        System.out.println("Customer Saved to database");
    }

    @Override
    public Customer findById(String id) {
        return repository.get(id);

    }

    @Override
    public void update(String id,Customer entity) {

            repository.remove(id);
            repository.put(id, entity);
            System.out.println("Customer Updated to database");

    }

    @Override
    public void delete(String id) {
        if(repository.get(id) == null) {
            System.out.println("Customer not found");
        } else {
            repository.remove(id);

        }
    }
}
