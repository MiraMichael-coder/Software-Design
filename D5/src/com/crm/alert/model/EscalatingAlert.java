package com.crm.alert.model;

// Decorator Pattern — Concrete Decorator #2: Escalation.

public class EscalatingAlert extends AlertModifier {

    public EscalatingAlert(SystemAlert alert) {
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
