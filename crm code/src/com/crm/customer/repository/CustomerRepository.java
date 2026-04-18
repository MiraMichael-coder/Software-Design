package com.crm.customer.repository;

<<<<<<< Updated upstream
import com.crm.customer.model.Customer;
import com.crm.persistence.repository.CrudRepository;

public class CustomerRepository implements CrudRepository<Customer> {

    @Override
    public void save(Customer entity) {
        // Skeleton: persist customer
=======
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
>>>>>>> Stashed changes
    }

    @Override
    public Customer findById(String id) {
<<<<<<< Updated upstream
        // Skeleton: fetch customer
        return null;
    }

    @Override
    public void update(Customer entity) {
        // Skeleton: update customer
=======
        return repository.get(id);

    }

    @Override
    public void update(String id,Customer entity) {

            repository.remove(id);
            repository.put(id, entity);
            System.out.println("Customer Updated to database");

>>>>>>> Stashed changes
    }

    @Override
    public void delete(String id) {
<<<<<<< Updated upstream
        // Skeleton: delete customer
=======
        if(repository.get(id) == null) {
            System.out.println("Customer not found");
        } else {
            repository.remove(id);

        }
>>>>>>> Stashed changes
    }
}
