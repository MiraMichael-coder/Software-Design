package com.crm.customer.sla;

import java.time.LocalDateTime;

// concrete strategy
public class StandardSlaCalculator implements SlaCalculator {
    @Override
    public LocalDateTime calculateDeadline(String priority, int baseSlaHours) {
        return LocalDateTime.now().plusHours(baseSlaHours);
    }
}
