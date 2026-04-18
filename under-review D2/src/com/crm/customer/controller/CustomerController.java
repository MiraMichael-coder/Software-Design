package com.crm.customer.controller;

import com.crm.customer.model.Customer;
import com.crm.customer.repository.CustomerRepository;

public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createCustomer(String id,Customer customer) {
        customerRepository.save( id,customer);
    }

    public Customer retrieveCustomer(String customerId) {
        return customerRepository.findById(customerId);
    }

    public void updateCustomer(String id,Customer customer) {
        customerRepository.update(id,customer);
    }

    public void deleteCustomer(String customerId) {
        if(customerRepository.findById(customerId)!=null){
            customerRepository.delete(customerId);
        }
        else
        {
            System.out.println("Customer not found");
        }

    }
}
