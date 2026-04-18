package com.crm.customer.repository;

import com.crm.customer.model.Complaint;
import com.crm.persistence.repository.CrudRepository;

import java.util.HashMap;
import java.util.Map;

public class ComplaintRepository implements CrudRepository<Complaint> {

    private final Map<String, Complaint> repository = new HashMap<>();

    @Override
    public void save(String id,Complaint entity) {
        repository.put(id, entity);
        System.out.println("Complaint saved in database");

    }

    @Override
    public Complaint findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id,Complaint entity)
    {
        repository.put(id, entity);
        System.out.println("Complaint updated");
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
        System.out.println("Complaint removed");

    }
}
