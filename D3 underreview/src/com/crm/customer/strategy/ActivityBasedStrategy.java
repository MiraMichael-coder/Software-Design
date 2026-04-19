package com.crm.customer.strategy;

import com.crm.customer.model.Customer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ActivityBasedStrategy implements SegmentationStrategy {
    private final int maxInactivityDays;

    public ActivityBasedStrategy(int maxInactivityDays) {
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
