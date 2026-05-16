package com.crm.customer.policies;

import com.crm.customer.model.Customer;

public interface SegmentationPolicy {
    boolean evaluate(Customer customer);
}
