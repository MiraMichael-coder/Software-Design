package com.crm.inventory.model;

import com.crm.common.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Product {
    private String productId;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private Money unitPrice;
    private int stockQuantity;
    @Setter
    private String supplierId;

    public Product(String productId, String name, String description, Money unitPrice, int stockQuantity, String supplierId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.supplierId = supplierId;
    }

    public void increaseStock(int quantity) {
        if (quantity > 0) {
            stockQuantity += quantity;
        }
    }

    public void decreaseStock(int quantity) {
        if (quantity > 0) {
            stockQuantity = Math.max(0, stockQuantity - quantity);
        }
    }

    public boolean isAvailable(int requestedQuantity) {
        return stockQuantity >= requestedQuantity;
    }

}
