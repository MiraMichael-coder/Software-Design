package com.crm.customer.policies;

import com.crm.customer.model.Customer;

public class GeographicSegmentation implements SegmentationPolicy {
    private final String targetRegion;

    public GeographicSegmentation(String targetRegion) {
        this.targetRegion = targetRegion;
    }

    @Override
    public boolean evaluate(Customer customer) {
        return targetRegion != null && targetRegion.equalsIgnoreCase(customer.getRegion());
    }
}
