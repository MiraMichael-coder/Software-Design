package com.crm.order.model;

import com.crm.common.Money;
import com.crm.inventory.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter

public class OrderItem {
    private final String orderItemId;
    private final Product product;
    @Setter
    private int quantity;
    @Setter
    private Money unitPrice;

    public OrderItem(String orderItemId, Product product, int quantity, Money unitPrice) {
        this.orderItemId = orderItemId;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Money calculateSubtotal() {
        if (unitPrice == null) {
            return null;
        }
        return new Money(unitPrice.getAmount() * quantity, unitPrice.getCurrency());
    }

}
