package com.crm.alert.providers;

import com.crm.alert.model.SlaBreachAlert;
import com.crm.alert.model.SystemAlert;

public class SlaBreachAlertProvider implements AlertProvider {
    @Override
    public SystemAlert createAlert(String alertId, String referenceId) {
        return new SlaBreachAlert(alertId, referenceId);
    }
}
