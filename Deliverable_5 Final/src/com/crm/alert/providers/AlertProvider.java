package com.crm.alert.providers;

import com.crm.alert.model.*;

public interface AlertProvider {
    SystemAlert createAlert(String alertId, String productId);
}
