package com.crm.alert.model;

//Abstract  Decorator
public abstract class AlertDecorator extends SystemAlert {

    protected final SystemAlert wrappedAlert;

    public AlertDecorator(SystemAlert alert) {
        super(alert.getAlertId(), alert.getSeverity());
        this.wrappedAlert = alert;
    }

    @Override
    public void notifyTarget() {
        wrappedAlert.notifyTarget();
    }

    @Override
    public void resolve() {
        wrappedAlert.resolve();
        this.resolved = true;
    }

    @Override
    public boolean isResolved() {
        return wrappedAlert.isResolved();
    }
}
