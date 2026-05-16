package com.crm.alert.providers;

import com.crm.alert.model.DeliveryDelayAlert;
import com.crm.alert.model.SystemAlert;

public class DeliveryDelayAlertProvider implements AlertProvider {
    @Override
    public SystemAlert createAlert(String alertId, String referenceId) {
        return new DeliveryDelayAlert(alertId, referenceId);
    }
}
