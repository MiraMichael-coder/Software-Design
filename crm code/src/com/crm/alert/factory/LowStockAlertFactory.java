package com.crm.alert.factory;

import com.crm.alert.model.LowStockAlert;
import com.crm.alert.model.SystemAlert;

public class LowStockAlertFactory implements AlertFactory {
    @Override
    public SystemAlert createAlert(String alertId, String referenceId) {
        return new LowStockAlert(alertId, referenceId);
    }
}
