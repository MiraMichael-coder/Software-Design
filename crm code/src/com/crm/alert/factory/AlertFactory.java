package com.crm.alert.factory;

import com.crm.alert.model.*;

public interface AlertFactory {
    SystemAlert createAlert(String alertId, String referenceId);

}
