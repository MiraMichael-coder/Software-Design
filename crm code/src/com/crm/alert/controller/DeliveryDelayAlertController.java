package com.crm.alert.controller;

import com.crm.alert.model.DeliveryDelayAlert;
import com.crm.alert.model.SystemAlert;
import com.crm.alert.repository.AlertRepository;

public class DeliveryDelayAlertController extends AlertController {
    public DeliveryDelayAlertController(AlertRepository alertRepository) {
        super(alertRepository);
    }

    @Override
    protected SystemAlert createAlert(String id, String ref) {
        return new DeliveryDelayAlert(id, ref);
    }
}
