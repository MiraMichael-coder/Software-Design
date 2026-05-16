package com.crm.customer.policies;

import com.crm.customer.model.Customer;

public class SpendingBasedSegmentation implements SegmentationPolicy {
    private final double minimumSpendingThreshold;

    public SpendingBasedSegmentation(double minimumSpendingThreshold) {
        this.minimumSpendingThreshold = minimumSpendingThreshold;
    }

    @Override
    public boolean evaluate(Customer customer) {
        return customer.getTotalSpending() >= minimumSpendingThreshold;
    }
}
