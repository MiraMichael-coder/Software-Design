package com.crm.alert.controller;

import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;

public abstract class AlertController {
    protected final AlertRepository alertRepository;
    protected final com.crm.alert.factory.AlertFactory alertFactory;

    public AlertController(AlertRepository alertRepository, com.crm.alert.factory.AlertFactory alertFactory) {
        this.alertRepository = alertRepository;
        this.alertFactory = alertFactory;
    }

    // This is the operation that uses the factory object
    public abstract SystemAlert processAlert(String id, String productId);

    public void resolveAlert(String alertId) {
        SystemAlert a = alertRepository.findById(alertId);
        if (a != null) {
            a.resolve();
            alertRepository.update(alertId, a);
        }

    }
}
