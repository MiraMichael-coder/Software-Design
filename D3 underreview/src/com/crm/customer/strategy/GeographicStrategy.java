package com.crm.customer.strategy;

import com.crm.customer.model.Customer;

public class GeographicStrategy implements SegmentationStrategy {
    private final String targetRegion;

    public GeographicStrategy(String targetRegion) {
        this.targetRegion = targetRegion;
    }

    @Override
    public boolean evaluate(Customer customer) {
        return targetRegion != null && targetRegion.equalsIgnoreCase(customer.getRegion());
    }
}
