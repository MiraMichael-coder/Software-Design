package com.crm.customer.SlaCalculator;

import java.time.LocalDateTime;

// concrete strategy
public class VipSlaCalculator implements SlaCalculator {
    @Override
    public LocalDateTime calculateDeadline(String priority, int baseSlaHours) {
        return LocalDateTime.now().plusHours(4);
    }
}
