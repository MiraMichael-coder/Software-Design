package com.crm.order.model;

// Decorator Pattern — Concrete Decorator for Logging delivery state changes.
public class LoggedDelivery extends DeliveryExtension {

    public LoggedDelivery(Delivery wrappedDelivery) {
        super(wrappedDelivery);
    }

    @Override
    public void markOnTheWay() {
        System.out.println("[ LOG ] Delivery " + getDeliveryId() + " marked ON THE WAY");
        super.markOnTheWay();
    }

    @Override
    public void markDelivered() {
        System.out.println("[ LOG ] Delivery " + getDeliveryId() + " marked DELIVERED");
        super.markDelivered();
    }

    @Override
    public void markDelayed() {
        System.out.println("[ LOG ] Delivery " + getDeliveryId() + " marked DELAYED");
        super.markDelayed();
    }
}
