package com.crm.order.model;

import com.crm.common.Money;

public class OrderItem {
    private String orderItemId;
    private int quantity;
    private Money unitPrice;
    private Product product;

    public OrderItem(String orderItemId, Product product, int quantity, Money unitPrice) {
        this.orderItemId = orderItemId;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getOrderItemId() { return orderItemId; }
    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Money getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Money unitPrice) { this.unitPrice = unitPrice; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Money calculateSubtotal() {
        if (unitPrice == null) return null;
        return new Money(unitPrice.getAmount() * quantity, unitPrice.getCurrency());
    }
}
