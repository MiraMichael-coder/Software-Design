package com.crm.alert.controller;

import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;
import com.crm.alert.providers.AlertProvider;

public abstract class AlertController {
    protected final AlertRepository alertRepository;
    protected final AlertProvider alertProvider;

    public AlertController(AlertRepository alertRepository, AlertProvider alertProvider) {
        this.alertRepository = alertRepository;
        this.alertProvider = alertProvider;
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
