package com.crm.customer.sla;

import java.time.LocalDateTime;

//Strategy pattern
public interface SlaCalculator {
    LocalDateTime calculateDeadline(String priority, int baseSlaHours);
}
