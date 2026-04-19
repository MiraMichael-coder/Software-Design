package com.crm.customer.SlaCalculator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// concrete strategy
public class PriorityBasedSlaCalculator implements SlaCalculator {
    private final Map<String, Integer> priorityHours = new HashMap<>();

    public PriorityBasedSlaCalculator(int baseSlaHours) {
        // Default priority mappings derived from the base SLA
        priorityHours.put("critical", 4);
        priorityHours.put("high", baseSlaHours / 2);
        priorityHours.put("medium", baseSlaHours);
        priorityHours.put("low", baseSlaHours * 2);
    }

    /*
     * Registers a custom SLA hour value for a given priority level.
     * This allows new priorities to be added without modifying the class,
     * keeping it open for extension and closed for modification.
     */
    public void registerPriority(String priority, int hours) {
        priorityHours.put(priority.toLowerCase(), hours);
    }

    @Override
    public LocalDateTime calculateDeadline(String priority, int baseSlaHours) {
        if (priority == null) {
            return LocalDateTime.now().plusHours(baseSlaHours);
        }

        int hours = priorityHours.getOrDefault(priority.toLowerCase(), baseSlaHours);
        return LocalDateTime.now().plusHours(hours);
    }
}
