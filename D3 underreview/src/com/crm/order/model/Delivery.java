package com.crm.order.model;

import com.crm.common.Address;
import com.crm.common.enums.DeliveryStatus;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Delivery {
    private final String deliveryId;
    private final String orderId;
    @Setter
    private Address address;
    @Setter
    private LocalDateTime eta;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveredAt;
    private DeliveryStatus deliveryStatus;

    public Delivery(String deliveryId, String orderId, Address address, LocalDateTime eta) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.address = address;
        this.eta = eta;
        this.deliveryStatus = DeliveryStatus.Preparing;
    }

    public void markOnTheWay() {
        this.deliveryStatus = DeliveryStatus.OnTheWay;
        this.dispatchedAt = LocalDateTime.now();
    }

    public void markDelivered() {
        this.deliveryStatus = DeliveryStatus.Delivered;
        this.deliveredAt = LocalDateTime.now();
    }

    public void markDelayed() {
        this.deliveryStatus = DeliveryStatus.Delayed;
    }

}
