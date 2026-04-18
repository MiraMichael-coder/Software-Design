package com.crm.customer.model;

import com.crm.common.Employee;
import com.crm.common.enums.ComplaintStatus;
import java.time.LocalDateTime;

public class Complaint {
    private String complaintId;
    private ComplaintStatus status;
    private String priority;
    private LocalDateTime slaDeadline;
    private LocalDateTime createdAt;
    private Employee assignedTo;

    public Complaint(String complaintId, String priority) {
        this.complaintId = complaintId;
        this.priority = priority;
        this.slaDeadline = LocalDateTime.now().plusHours(24);;
        this.status = ComplaintStatus.Open;
        this.createdAt = LocalDateTime.now();
    }

    public String getComplaintId() { return complaintId; }
    public void setComplaintId(String complaintId) { this.complaintId = complaintId; }

    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public LocalDateTime getSlaDeadline() { return slaDeadline; }
    public void setSlaDeadline(LocalDateTime slaDeadline) { this.slaDeadline = slaDeadline; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Employee getAssignedTo() { return assignedTo; }

    public void assignTo(Employee employee) {
        this.assignedTo = employee;
        this.status = ComplaintStatus.InProgress;
    }

    public void updateStatus(ComplaintStatus status) {
        this.status = status;
    }

    public boolean isSlaBreached() {
        return slaDeadline != null && LocalDateTime.now().isAfter(slaDeadline) && status != ComplaintStatus.Resolved;
    }
    public void  ComplaintDetails()
    {
        System.out.println("Complaint Details:");
        System.out.println("ComplaintId: " + complaintId);
        System.out.println("Priority: " + priority);
        System.out.println("SlaDeadline: " + slaDeadline);
        System.out.println("CreatedAt: " + createdAt);
        System.out.println("AssignedTo: " + assignedTo.getName());
        System.out.println("Status: " + status);

    }
}
