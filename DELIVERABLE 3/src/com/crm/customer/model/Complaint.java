package com.crm.customer.model;

import com.crm.common.Employee;
import com.crm.common.enums.ComplaintStatus;
import com.crm.customer.SlaCalculator.SlaCalculator;

import java.time.LocalDateTime;
import com.crm.persistence.SlaConfigurationManager;

import lombok.*;

@Getter
@Setter
public class Complaint {
    private String complaintId;
    private ComplaintStatus status;
    private String priority;
    private LocalDateTime slaDeadline;
    private LocalDateTime createdAt;
    private Employee assignedTo;

    public Complaint(String complaintId, String priority, SlaCalculator slaCalculator) {
        this.complaintId = complaintId;
        this.priority = priority;
        int baseSla = SlaConfigurationManager.getInstance().getSlaHours();
        this.slaDeadline = slaCalculator.calculateDeadline(priority, baseSla);
        this.status = ComplaintStatus.Open;
        this.createdAt = LocalDateTime.now();
    }

    public void assignTo(Employee employee) {
        this.assignedTo = employee;
        this.status = ComplaintStatus.InProgress;
    }

    public void updateStatus(ComplaintStatus status) {
        this.status = status;
    }

    public boolean isSlaBreached() {
        return slaDeadline != null && LocalDateTime.now().isAfter(slaDeadline)
                && status != ComplaintStatus.Resolved;
    }

    public void ComplaintDetails() {
        System.out.println("Complaint Details:");
        System.out.println("ComplaintId: " + complaintId);
        System.out.println("Priority: " + priority);
        System.out.println("SlaDeadline: " + slaDeadline);
        System.out.println("CreatedAt: " + createdAt);
        System.out.println("AssignedTo: " + assignedTo.getName());
        System.out.println("Status: " + status);

    }
}
