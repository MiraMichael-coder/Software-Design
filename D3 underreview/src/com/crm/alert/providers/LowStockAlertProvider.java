package com.crm.alert.providers;

import com.crm.alert.model.LowStockAlert;
import com.crm.alert.model.SystemAlert;

public class LowStockAlertProvider implements AlertProvider {
    @Override
    public SystemAlert createAlert(String alertId, String productId) {
        return new LowStockAlert(alertId, productId);
    }
}
