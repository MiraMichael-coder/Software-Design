package com.crm.inventory.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InventoryRecord {
    private String recordId;
    private String productId;
    private int availableQuantity;
    private int reorderLevel;
    private LocalDateTime lastUpdatedAt;

    public InventoryRecord(String recordId, String productId, int availableQuantity, int reorderLevel) {
        this.recordId = recordId;
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.reorderLevel = reorderLevel;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void increase(int quantity) {
        if (quantity > 0) {
            availableQuantity += quantity;
            touch();
        }
    }

    public void decrease(int quantity) {
        if (quantity > 0) {
            availableQuantity = Math.max(0, availableQuantity - quantity);
            touch();
        }
    }

    public boolean isBelowReorderLevel() {
        return availableQuantity <= reorderLevel;
    }

    private void touch() {
        lastUpdatedAt = LocalDateTime.now();
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
        touch();
    }
}
