package com.crm.order.model;

import com.crm.common.Address;
import com.crm.common.enums.DeliveryStatus;
import java.time.LocalDateTime;

public class Delivery {
    private String deliveryId;
    private Address address;
    private LocalDateTime eta;
    private DeliveryStatus deliveryStatus;

    public Delivery(String deliveryId, Address address, LocalDateTime eta) {
        this.deliveryId = deliveryId;
        this.address = address;
        this.eta = eta;
        this.deliveryStatus = DeliveryStatus.Preparing;
    }

    public String getDeliveryId() { return deliveryId; }
    public void setDeliveryId(String deliveryId) { this.deliveryId = deliveryId; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public LocalDateTime getEta() { return eta; }
    public void setEta(LocalDateTime eta) { this.eta = eta; }

    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }

    public void updateStatus(DeliveryStatus status) {
        this.deliveryStatus = status;
    }
}
