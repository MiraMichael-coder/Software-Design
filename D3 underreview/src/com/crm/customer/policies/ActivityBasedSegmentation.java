package com.crm.customer.policies;

import com.crm.customer.model.Customer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ActivityBasedSegmentation implements SegmentationPolicy {
    private final int maxInactivityDays;

    public ActivityBasedSegmentation(int maxInactivityDays) {
        this.maxInactivityDays = maxInactivityDays;
    }

    @Override
    public boolean evaluate(Customer customer) {
        if (customer.getLastActivityDate() == null) {
            return false;
        }
        long daysInactive = ChronoUnit.DAYS.between(customer.getLastActivityDate(), LocalDateTime.now());
        return daysInactive <= maxInactivityDays;
    }
}
