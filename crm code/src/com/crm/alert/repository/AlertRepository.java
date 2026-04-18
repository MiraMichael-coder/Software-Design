package com.crm.alert.repository;
<<<<<<< Updated upstream
=======
import java.util.HashMap;
import java.util.Map;
>>>>>>> Stashed changes

import com.crm.alert.model.SystemAlert;
import com.crm.persistence.repository.CrudRepository;

public class AlertRepository implements CrudRepository<SystemAlert> {

<<<<<<< Updated upstream
    @Override
    public void save(SystemAlert entity) { }

    @Override
    public SystemAlert findById(String id) { return null; }

    @Override
    public void update(SystemAlert entity) { }

    @Override
    public void delete(String id) { }
}
=======
    private final Map<String, SystemAlert> repository = new HashMap<>();

    @Override
    public void save(String id,SystemAlert entity) {
        repository.put(id, entity);
        System.out.println("Alert Saved to database");
    }

    @Override
    public SystemAlert findById(String id) {
        return repository.get(id);

    }

    @Override
    public void update(String id, SystemAlert entity) {
        if (repository.containsKey(id)) {
            repository.put(id, entity);
            System.out.println("Alert Updated to database");
        }
        else {
            System.out.println("Alert not found");
        }
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
    System.out.println("Alert deleted from database");
    }
}


>>>>>>> Stashed changes
