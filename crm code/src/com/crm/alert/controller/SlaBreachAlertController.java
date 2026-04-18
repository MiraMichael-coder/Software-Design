package com.crm.alert.controller;

import com.crm.alert.model.SlaBreachAlert;
import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;

public class SlaBreachAlertController extends AlertController {
    public SlaBreachAlertController(AlertRepository alertRepository) {
        super(alertRepository);
    }

    @Override
    protected SystemAlert createAlert(String id, String ref) {
        return new SlaBreachAlert(id, ref);
    }
}
