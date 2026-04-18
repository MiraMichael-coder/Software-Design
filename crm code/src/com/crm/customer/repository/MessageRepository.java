package com.crm.customer.repository;

import com.crm.customer.model.Message;
import com.crm.persistence.repository.CrudRepository;

<<<<<<< Updated upstream
public class MessageRepository implements CrudRepository<Message> {

    @Override
    public void save(Message entity) { }

    @Override
    public Message findById(String id) { return null; }

    @Override
    public void update(Message entity) { }

    @Override
    public void delete(String id) { }
=======
import java.util.HashMap;
import java.util.Map;

public class MessageRepository implements CrudRepository<Message> {
    private final Map<String, Message> repository = new HashMap<>();

    @Override
    public void save(String id, Message entity) {
        repository.put(id, entity);
        System.out.println("Message saved in database");
    }

    @Override
    public Message findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, Message entity) {
        repository.put(id, entity);
        System.out.println("Message updated");
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
        System.out.println("Message removed");
    }
>>>>>>> Stashed changes
}
