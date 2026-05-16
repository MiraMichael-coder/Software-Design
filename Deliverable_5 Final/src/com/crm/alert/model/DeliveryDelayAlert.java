package com.crm.alert.model;

import com.crm.common.enums.AlertSeverity;

public class DeliveryDelayAlert extends SystemAlert {
    private String orderId;

    public DeliveryDelayAlert(String alertId, String orderId) {
        super(alertId, AlertSeverity.High);
        this.orderId = orderId;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    @Override
    public void notifyTarget() {
        System.out.println("DeliveryDelayAlert -> notify customer about order " + orderId);
    }
}
