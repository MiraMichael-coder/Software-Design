package com.crm.order.model;

import com.crm.common.Money;

public class Product {
    private String productId;
    private String name;
    private Money price;
    private int stockQuantity;

    public Product(String productId, String name, Money price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Money getPrice() { return price; }
    public void setPrice(Money price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public void updateStock(int delta) {
        this.stockQuantity += delta;
    }

    public boolean isAvailable(int qty) {
        return stockQuantity >= qty;
    }
}
