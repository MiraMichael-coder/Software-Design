package com.crm.alert.controller;

import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;
import com.crm.alert.providers.DeliveryDelayAlertProvider;

public class DeliveryDelayAlertController extends AlertController {
    public DeliveryDelayAlertController(AlertRepository alertRepository) {
        super(alertRepository, new DeliveryDelayAlertProvider());
    }

    @Override
    public SystemAlert processAlert(String id, String referenceId) {
        SystemAlert alert = alertProvider.createAlert(id, referenceId);
        alertRepository.save(id, alert);
        return alert;
    }
}
