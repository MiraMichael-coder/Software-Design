package com.crm.order.model;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class HVOrderitem implements Iterator<OrderItem> {

    private final List<OrderItem> items;
    private final double minPrice;
    private int currentIndex;
    private OrderItem nextItem;

    // recieve full item list from order
    public HVOrderitem(List<OrderItem> items, double minPrice) {
        this.items = items;
        this.minPrice = minPrice;
        this.currentIndex = 0;
        advance();
    }

    // the engine
    private void advance() {
        nextItem = null;
        // moving current index one step at a time
        while (currentIndex < items.size()) {
            OrderItem candidate = items.get(currentIndex++);
            if (candidate != null
                    && candidate.calculateSubtotal() != null
                    && candidate.calculateSubtotal().getAmount() > minPrice) {
                nextItem = candidate;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() { // checks if advasnce()found a qualifying item
        return nextItem != null;
    }

    @Override
    public OrderItem next() { // saves the current next to return calls advance()
        if (!hasNext()) {
            throw new NoSuchElementException("No more high-value items.");
        }
        OrderItem result = nextItem;
        advance();
        return result;
    }
}
