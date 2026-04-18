package com.crm.order.model;

// Decorator Pattern — Concrete Decorator for Notifying customers of delivery state changes.
public class NotifyingDeliveryDecorator extends DeliveryDecorator {

    public NotifyingDeliveryDecorator(Delivery wrappedDelivery) {
        super(wrappedDelivery);
    }

    @Override
    public void markDelivered() {
        super.markDelivered();
        System.out.println("[ NOTIFY ] Customer notified: Order " + getOrderId() + " has been delivered!");
    }

    @Override
    public void markDelayed() {
        super.markDelayed();
        System.out.println("[ NOTIFY ] Customer notified: Order " + getOrderId() + " is delayed.");
    }

    @Override
    public void markOnTheWay() {
        super.markOnTheWay();
    }
}
