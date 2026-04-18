package com.crm.order.model;

import com.crm.common.Money;
import com.crm.common.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String customerId;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private LocalDateTime lastUpdatedAt;
    private Money totalAmount;
    private List<OrderItem> items;
    private Delivery delivery;

    public Order(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.Pending;
        this.lastUpdatedAt = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.totalAmount = new Money(0, "EGP");
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public OrderStatus getStatus() { return status; }

    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }

    public Money getTotalAmount() { return totalAmount; }

    public List<OrderItem> getItems() { return items; }

    public Delivery getDelivery() { return delivery; }
    public void setDelivery(Delivery delivery) { this.delivery = delivery; }

    public void addItem(OrderItem item) {
        if (item != null) items.add(item);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    public Money calculateTotal() {
        double sum = 0;
        String currency = totalAmount != null ? totalAmount.getCurrency() : "EGP";
        for (OrderItem it : items) {
            Money sub = it.calculateSubtotal();
            if (sub != null) sum += sub.getAmount();
        }
        this.totalAmount = new Money(sum, currency);
        return this.totalAmount;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void cancel() {
        updateStatus(OrderStatus.Cancelled);
    }
}
