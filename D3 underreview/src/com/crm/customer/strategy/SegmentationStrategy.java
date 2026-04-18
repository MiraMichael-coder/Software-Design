package com.crm.customer.strategy;

import com.crm.customer.model.Customer;

public interface SegmentationStrategy {
    boolean evaluate(Customer customer);
}
