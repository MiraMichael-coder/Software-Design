package com.crm.alert.model;

// Decorator Pattern — Concrete Decorator #2: Escalation.

public class EscalatingAlertDecorator extends AlertDecorator {

    public EscalatingAlertDecorator(SystemAlert alert) {
        super(alert);
    }

    @Override
    public void notifyTarget() {
        super.notifyTarget(); // delegate first

        System.out.println("[ ESCALATE ] Alert " + alertId
                + " (Severity: " + severity + ")"
                + " has been escalated to management.");
    }
}
