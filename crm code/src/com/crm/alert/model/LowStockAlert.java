package com.crm.alert.model;

import com.crm.common.enums.AlertSeverity;

public class LowStockAlert extends SystemAlert {
    private String productId;

    public LowStockAlert(String alertId, String productId) {
        super(alertId, AlertSeverity.Medium);
        this.productId = productId;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    @Override
    public void notifyTarget() {
        // Skeleton: no real notifications
        System.out.println("LowStockAlert -> notify inventory for product " + productId);
    }
}
