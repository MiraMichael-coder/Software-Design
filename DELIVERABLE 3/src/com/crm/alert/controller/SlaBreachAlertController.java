package com.crm.alert.controller;

import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;
import com.crm.alert.providers.SlaBreachAlertProvider;

public class SlaBreachAlertController extends AlertController {
    public SlaBreachAlertController(AlertRepository alertRepository) {
        super(alertRepository, new SlaBreachAlertProvider());
    }

    @Override
    public SystemAlert processAlert(String id, String referenceId) {
        SystemAlert alert = alertProvider.createAlert(id, referenceId);
        alertRepository.save(id, alert);
        return alert;
    }
}
