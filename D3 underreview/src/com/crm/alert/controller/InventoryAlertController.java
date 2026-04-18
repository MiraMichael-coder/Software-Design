package com.crm.alert.controller;

import com.crm.alert.factory.LowStockAlertFactory;
import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;
import com.crm.inventory.event.StockChangedEvent;
import com.crm.inventory.event.StockObserver;

//Listener
public class InventoryAlertController extends AlertController {

    public InventoryAlertController(AlertRepository alertRepository) {
        super(alertRepository, new LowStockAlertFactory());
    }

    // Factory Method
    @Override
    public SystemAlert processAlert(String id, String productId) {
        SystemAlert alert = alertFactory.createAlert(id, productId);
        alertRepository.save(id, alert);
        return alert;
    }

}
