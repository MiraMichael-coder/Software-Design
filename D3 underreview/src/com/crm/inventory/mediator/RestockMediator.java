package com.crm.inventory.mediator;

public interface RestockMediator {
    void onLowStock(String recordId, String productId);
}
