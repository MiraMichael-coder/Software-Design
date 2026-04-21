package com.crm.inventory.event;

//Observer interface for the Inventory Stock Change Notification System.
//interface for observers 
public interface StockListener {

    void onStockChanged(StockChangedEvent event);
}
