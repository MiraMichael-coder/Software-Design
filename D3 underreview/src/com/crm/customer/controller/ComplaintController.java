package com.crm.customer.controller;

import com.crm.common.Employee;
import com.crm.communication.providers.CommunicationChannelProvider;
import com.crm.customer.model.Complaint;
import com.crm.customer.model.ComplaintEscalationHandler;
import com.crm.customer.model.Customer;
import com.crm.customer.repository.ComplaintRepository;

public class ComplaintController {
    private final ComplaintRepository complaintRepository;
    // mediator
    private ComplaintEscalationHandler mediator;

    public ComplaintController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public void setMediator(ComplaintEscalationHandler mediator) {
        this.mediator = mediator;
    }

    public void createComplaint(Complaint complaint) {
        complaintRepository.save(complaint.getComplaintId(), complaint);
    }

    public Complaint getComplaint(String complaintId) {
        return complaintRepository.findById(complaintId);
    }

    public void updateComplaint(Complaint complaint) {
        complaintRepository.update(complaint.getComplaintId(), complaint);
    }

    public void deleteComplaint(String complaintId) {
        complaintRepository.delete(complaintId);
    }

    public void detectSlaBreach(Complaint complaint, Customer customer,
            Employee supervisor, CommunicationChannelProvider factory) {
        
        // Ensure that the complaint is ACTUALLY breached, and NOT already resolved
        if (!complaint.isSlaBreached()) {
            System.out.println("No breach detected or complaint is already resolved. Skipping SLA escalation.");
            return;
        }

        System.out.println("Detected SLA breach for complaint " + complaint.getComplaintId());

        if (this.mediator != null) {
            this.mediator.onSlaBreached(complaint, customer, supervisor, factory);
        }
    }
}
