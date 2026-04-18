package com.crm.customer.repository;

import com.crm.customer.model.Complaint;
import com.crm.persistence.repository.CrudRepository;

<<<<<<< Updated upstream
public class ComplaintRepository implements CrudRepository<Complaint> {

    @Override
    public void save(Complaint entity) { }

    @Override
    public Complaint findById(String id) { return null; }

    @Override
    public void update(Complaint entity) { }

    @Override
    public void delete(String id) { }
=======
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
>>>>>>> Stashed changes
}
