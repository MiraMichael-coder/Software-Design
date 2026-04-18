package com.crm.inventory.event;

//Observer interface for the Inventory Stock Change Notification System.
//interface for observers 
public interface StockObserver {

    void onStockChanged(StockChangedEvent event);
}
