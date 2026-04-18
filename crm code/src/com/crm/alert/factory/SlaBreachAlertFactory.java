package com.crm.alert.factory;

import com.crm.alert.model.SlaBreachAlert;
import com.crm.alert.model.SystemAlert;

public class SlaBreachAlertFactory implements AlertFactory {
    @Override
    public SystemAlert createAlert(String alertId, String referenceId) {
        return new SlaBreachAlert(alertId, referenceId);
    }
}
