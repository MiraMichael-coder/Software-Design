package com.crm.inventory.event;

import java.util.ArrayList;
import java.util.List;

import com.crm.common.Money;

//Singleton INSTANCE TO BE CONSISTENT

//Subject responsible for inventory changes and notifying observerss

public final class InventoryEventManager {

    private static volatile InventoryEventManager instance;

    private InventoryEventManager() {
    }

    // double check lock
    public static InventoryEventManager getInstance() {
        if (instance == null) {
            synchronized (InventoryEventManager.class) {
                if (instance == null) {
                    instance = new InventoryEventManager();
                }
            }
        }
        return instance;
    }

    private final List<StockObserver> observers = new ArrayList<>();

    // add observer
    public void register(StockObserver observer) {
        if (observer == null)
            System.out.println("observer is null");
        else {
            if (!observers.contains(observer)) {
                observers.add(observer);
            }
        }
    }

    // remove observer
    public void unregister(StockObserver observer) {
        observers.remove(observer);
    }

    // notify observers about change
    public void notify(String productId, String supplierId, int newQuantity, Money unitPrice) {
        StockChangedEvent event = new StockChangedEvent(productId, supplierId, newQuantity, unitPrice);

        for (StockObserver ob : observers) {
            ob.onStockChanged(event);
        }
    }
}
