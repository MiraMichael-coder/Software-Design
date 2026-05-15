package com.crm.customer.repository;

import com.crm.customer.model.Message;
import com.crm.persistence.repository.CrudRepository;

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
}
