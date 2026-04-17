package com.crm.alert.model;

import com.crm.common.DateTimeUtil;

// Decorator Pattern — Concrete Decorator #1: Logging.

public class LoggingAlertDecorator extends AlertDecorator {

    public LoggingAlertDecorator(SystemAlert alert) {
        super(alert);
    }

    @Override
    public void notifyTarget() {
        System.out.println("[ LOG ] Alert " + alertId
                + " | Severity: " + severity
                + " | Triggered at: " + DateTimeUtil.now());

        super.notifyTarget(); // delegate to wrapped alert

        System.out.println("[ LOG ] Alert " + alertId + " notification completed.");
    }
}
