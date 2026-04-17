package com.crm.alert.model;

import com.crm.common.enums.AlertSeverity;
import com.crm.common.DateTimeUtil;
import java.time.LocalDateTime;

public abstract class SystemAlert {
    protected String alertId;
    protected AlertSeverity severity;
    protected LocalDateTime createdAt;
    protected boolean resolved;

    public SystemAlert(String alertId, AlertSeverity severity) {
        this.alertId = alertId;
        this.severity = severity;
        this.createdAt = LocalDateTime.now();
        this.resolved = false;
    }


    public String getAlertId() { return alertId; }
    public AlertSeverity getSeverity() { return severity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getFormattedCreatedAt() { return DateTimeUtil.format(createdAt); }
    public boolean isResolved() { return resolved; }

    public void resolve() { this.resolved = true; }



    // Abstract behavior required by professor-style skeleton
    public abstract void notifyTarget();
}
