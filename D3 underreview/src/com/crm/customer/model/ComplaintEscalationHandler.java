package com.crm.customer.model;

import com.crm.common.Employee;
import com.crm.communication.factory.CommunicationFactory;

public interface ComplaintEscalationHandler {
    void onSlaBreached(Complaint complaint, Customer customer, Employee supervisor, CommunicationFactory factory);
}
