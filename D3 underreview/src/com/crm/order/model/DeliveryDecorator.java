package com.crm.order.model;

import com.crm.common.enums.DeliveryStatus;

// Decorator Pattern — Base Decorator for Delivery status transitions.
public abstract class DeliveryDecorator extends Delivery {
    protected Delivery wrappedDelivery;

    public DeliveryDecorator(Delivery delivery) {
        super(delivery.getDeliveryId(), delivery.getOrderId(), delivery.getAddress(), delivery.getEta());
        this.wrappedDelivery = delivery;
    }

    @Override
    public String getDeliveryId() {
        return wrappedDelivery.getDeliveryId();
    }

    @Override
    public String getOrderId() {
        return wrappedDelivery.getOrderId();
    }

    @Override
    public DeliveryStatus getDeliveryStatus() {
        return wrappedDelivery.getDeliveryStatus();
    }

    @Override
    public void markOnTheWay() {
        wrappedDelivery.markOnTheWay();
    }

    @Override
    public void markDelivered() {
        wrappedDelivery.markDelivered();
    }

    @Override
    public void markDelayed() {
        wrappedDelivery.markDelayed();
    }
}
