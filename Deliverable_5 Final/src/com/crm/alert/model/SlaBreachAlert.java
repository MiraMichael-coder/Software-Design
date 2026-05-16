package com.crm.alert.model;

import com.crm.common.enums.AlertSeverity;

public class SlaBreachAlert extends SystemAlert {
    private String complaintId;

    public SlaBreachAlert(String alertId, String complaintId) {
        super(alertId, AlertSeverity.Critical);
        this.complaintId = complaintId;
    }

    public String getComplaintId() { return complaintId; }
    public void setComplaintId(String complaintId) { this.complaintId = complaintId; }

    @Override
    public void notifyTarget() {
        System.out.println("SlaBreachAlert -> escalate complaint " + complaintId);
    }
}
