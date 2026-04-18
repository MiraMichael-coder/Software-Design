package com.crm.inventory.model;

import com.crm.common.Money;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PurchaseOrder {
    private final String purchaseOrderId;
    private final String supplierId;
    private final String productId;
    private final int quantity;
    private final Money totalCost;
    private final LocalDateTime createdAt;
    private boolean received;

    public PurchaseOrder(String purchaseOrderId, String supplierId, String productId, int quantity, Money totalCost) {
        this.purchaseOrderId = purchaseOrderId;
        this.supplierId = supplierId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.createdAt = LocalDateTime.now();
        this.received = false;
    }

    public void markReceived() {
        this.received = true;
    }

}
