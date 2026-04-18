package com.crm.payment.repository;

import com.crm.payment.model.PaymentTransaction;
import com.crm.persistence.repository.CrudRepository;

<<<<<<< Updated upstream
public class PaymentRepository implements CrudRepository<PaymentTransaction> {

    @Override
    public void save(PaymentTransaction entity) { }

    @Override
    public PaymentTransaction findById(String id) { return null; }

    @Override
    public void update(PaymentTransaction entity) { }

    @Override
    public void delete(String id) { }
=======
import java.util.HashMap;
import java.util.Map;

public class PaymentRepository implements CrudRepository<PaymentTransaction> {
    private final Map<String, PaymentTransaction> repository = new HashMap<>();

    @Override
    public void save(String id, PaymentTransaction entity) {
        repository.put(id, entity);
        System.out.println("Payment saved in database");
    }

    @Override
    public PaymentTransaction findById(String id) {
        return repository.get(id);
    }

    @Override
    public void update(String id, PaymentTransaction entity) {
        repository.put(id, entity);
        System.out.println("Payment updated");
    }

    @Override
    public void delete(String id) {
        repository.remove(id);
        System.out.println("Payment removed");
    }
>>>>>>> Stashed changes
}
