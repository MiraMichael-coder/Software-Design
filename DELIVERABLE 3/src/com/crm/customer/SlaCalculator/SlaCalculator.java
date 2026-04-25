package com.crm.customer.SlaCalculator;

import java.time.LocalDateTime;

//Strategy pattern
public interface SlaCalculator {
    LocalDateTime calculateDeadline(String priority, int baseSlaHours);
}
