package com.crm.customer.model;

import com.crm.alert.controller.SlaBreachAlertController;
import com.crm.communication.controller.CommunicationController;
import com.crm.communication.providers.CommunicationChannelProvider;
import com.crm.customer.controller.ComplaintController;
import com.crm.common.Employee;

public class ComplaintEscalationHandlerService implements ComplaintEscalationHandler {

    // recive all three contorollers from different classes
    // only mediator knows about them but the 3 controllers dont have access on
    // eachother
    private final ComplaintController complaintController;
    private final SlaBreachAlertController slaAlertController;
    private final CommunicationController communicationController;

    public ComplaintEscalationHandlerService(
            ComplaintController complaintController,
            SlaBreachAlertController slaAlertController,
            CommunicationController communicationController) {
        this.complaintController = complaintController;
        this.slaAlertController = slaAlertController;
        this.communicationController = communicationController;
    }

    @Override
    public void onSlaBreached(Complaint complaint, Customer customer, Employee supervisor,
            CommunicationChannelProvider factory) {
        System.out.println("[Mediator] SLA breached detected for complaint: " + complaint.getComplaintId());

        complaint.assignTo(supervisor);
        complaintController.updateComplaint(complaint);
        // step 1 assigning complaint to supervisor
        System.out.println("Complaint reassigned to supervisor: " + supervisor.getName());

        slaAlertController.processAlert("ALT-SLA-" + complaint.getComplaintId(), complaint.getComplaintId());
        // step 2 raising sla breach alert
        System.out.println("SLA breach alert raised for complaint: " + complaint.getComplaintId());

        communicationController.sendMessage(
                factory,
                customer.getEmail(),
                "Dear " + customer.getName() + ", your complaint [" + complaint.getComplaintId()
                        + "] has been escalated to a supervisor due to an SLA breach.");
        // step 3 notifying customer about the escalation
        System.out.println("Customer [" + customer.getName() + "] notified via "
                + factory.getClass().getSimpleName() + ".");
    }
}

/*
 * Caller (Main.java)
 * │
 * └── mediator.onSlaBreached(complaint, customer, supervisor, factory)
 * │
 * ├── Step 1 → complaintController.updateComplaint(...)
 * ├── Step 2 → slaAlertController.processAlert(...)
 * └── Step 3 → communicationController.sendMessage(...)
 */
