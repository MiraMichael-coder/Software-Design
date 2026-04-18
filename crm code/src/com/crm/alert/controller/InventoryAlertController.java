package com.crm.alert.controller;

import com.crm.alert.model.LowStockAlert;
import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;

public class InventoryAlertController extends AlertController {
    public InventoryAlertController(AlertRepository alertRepository) {
        super(alertRepository);
    }

    @Override
    protected SystemAlert createAlert(String id, String ref) {
        return new LowStockAlert(id, ref);
    }
}
