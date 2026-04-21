package com.crm.alert.controller;

import com.crm.alert.providers.LowStockAlertProvider;
import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;
import com.crm.inventory.event.StockChangedEvent;
import com.crm.inventory.event.StockListener;

//Listener

public class InventoryAlertController extends AlertController implements StockListener {


    public InventoryAlertController(AlertRepository alertRepository) {
        super(alertRepository, new LowStockAlertProvider());
    }

    // Factory Method
    @Override
    public SystemAlert processAlert(String id, String productId) {
        SystemAlert alert = alertProvider.createAlert(id, productId);
        alertRepository.save(id, alert);
        return alert;
    }

}
