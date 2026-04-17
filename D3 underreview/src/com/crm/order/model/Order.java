package com.crm.order.model;

import com.crm.common.Money;
import com.crm.common.enums.OrderStatus;
import com.crm.payment.model.PaymentTransaction;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Order {

    private final String orderId;
    private final String customerId;
    private final LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private OrderStatus status;
    private final List<OrderItem> items;
    private Money totalAmount;
    private Delivery delivery;
    private String paymentId;

    public Order(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = this.createdAt;
        this.status = OrderStatus.Pending;
        this.items = new ArrayList<>();
        this.totalAmount = new Money(0, "EGP");
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            return;
        }
        items.add(item);
        recalculateTotal();
        touch();
    }

    public void removeItemById(String orderItemId) {
        items.removeIf(item -> item.getOrderItemId().equals(orderItemId));
        recalculateTotal();
        touch();
    }

    public Money recalculateTotal() {
        double sum = 0;
        String currency = totalAmount != null ? totalAmount.getCurrency() : "EGP";

        for (OrderItem item : items) {
            if (item == null || item.calculateSubtotal() == null) {
                continue;
            }
            sum += item.calculateSubtotal().getAmount();
            currency = item.calculateSubtotal().getCurrency();
        }

        totalAmount = new Money(sum, currency);
        return totalAmount;
    }

    public void updateStatus(OrderStatus status) {
        if (status == null) {
            return;
        }
        this.status = status;
        touch();
    }

    public void cancel() {
        this.status = OrderStatus.Cancelled;
        touch();
    }

    public void attachDelivery(Delivery delivery) {
        this.delivery = delivery;
        touch();
    }

    public void attachPayment(PaymentTransaction paymentTransaction) {
        this.paymentId = paymentTransaction == null ? null : paymentTransaction.getPaymentId();
        touch();
    }

    private void touch() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

}
