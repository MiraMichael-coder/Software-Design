package com.crm.alert.controller;

import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;
import com.crm.alert.factory.SlaBreachAlertFactory;

public class SlaBreachAlertController extends AlertController {
    public SlaBreachAlertController(AlertRepository alertRepository) {
        super(alertRepository, new SlaBreachAlertFactory());
    }

    @Override
    public SystemAlert processAlert(String id, String referenceId) {
        SystemAlert alert = alertFactory.createAlert(id, referenceId);
        alertRepository.save(id, alert);
        return alert;
    }
}
