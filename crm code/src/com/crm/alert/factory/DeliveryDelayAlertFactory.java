package com.crm.alert.factory;

import com.crm.alert.model.DeliveryDelayAlert;
import com.crm.alert.model.SystemAlert;

public class DeliveryDelayAlertFactory implements AlertFactory {
    @Override
    public SystemAlert createAlert(String alertId, String referenceId) {
        return new DeliveryDelayAlert(alertId, referenceId);
    }
}
