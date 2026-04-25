package com.crm.inventory.event;

import java.time.LocalDateTime;

import com.crm.common.Money;
import lombok.Getter;

// Immutable value object that carries the details of a stock-change

@Getter
public final class StockChangedEvent {

    private final String productId;
    private final String supplierId;
    private final int newQuantity;
    private final Money unitPrice;
    private final LocalDateTime occurredAt;

    public StockChangedEvent(String productId, String supplierId, int newQuantity, Money unitPrice) {
        this.productId = productId;
        this.supplierId = supplierId;
        this.newQuantity = newQuantity;
        this.unitPrice = unitPrice;
        this.occurredAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "StockChangedEvent{productId='" + productId
                + "', supplierId='" + supplierId
                + "', newQuantity=" + newQuantity
                + "', unitPrice=" + unitPrice
                + ", at=" + occurredAt + "}";
    }
}
