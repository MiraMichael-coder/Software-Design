package com.crm.order.model;

import com.crm.alert.model.DeliveryDelayAlert;

// Decorator Pattern — Concrete Decorator for Alerting on significant delivery state changes.
public class AlertingDeliveryDecorator extends DeliveryDecorator {

    public AlertingDeliveryDecorator(Delivery wrappedDelivery) {
        super(wrappedDelivery);
    }

    @Override
    public void markDelayed() {
        super.markDelayed();
        DeliveryDelayAlert alert = new DeliveryDelayAlert("ALERT-" + getOrderId(), getOrderId());
        alert.notifyTarget();
    }

    @Override
    public void markOnTheWay() {
        super.markOnTheWay();
    }

    @Override
    public void markDelivered() {
        super.markDelivered();
    }
}
