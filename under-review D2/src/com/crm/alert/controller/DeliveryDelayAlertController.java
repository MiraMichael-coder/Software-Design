package com.crm.alert.controller;

import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;
import com.crm.alert.factory.DeliveryDelayAlertFactory;

public class DeliveryDelayAlertController extends AlertController {
    public DeliveryDelayAlertController(AlertRepository alertRepository) {
        super(alertRepository, new DeliveryDelayAlertFactory());
    }

    @Override
    public SystemAlert processAlert(String id, String referenceId) {
        SystemAlert alert = alertFactory.createAlert(id, referenceId);
        alertRepository.save(id, alert);
        return alert;
    }
}
