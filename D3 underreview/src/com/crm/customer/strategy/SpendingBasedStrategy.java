package com.crm.customer.strategy;

import com.crm.customer.model.Customer;

public class SpendingBasedStrategy implements SegmentationStrategy {
    private final double minimumSpendingThreshold;

    public SpendingBasedStrategy(double minimumSpendingThreshold) {
        this.minimumSpendingThreshold = minimumSpendingThreshold;
    }

    @Override
    public boolean evaluate(Customer customer) {
        return customer.getTotalSpending() >= minimumSpendingThreshold;
    }
}
