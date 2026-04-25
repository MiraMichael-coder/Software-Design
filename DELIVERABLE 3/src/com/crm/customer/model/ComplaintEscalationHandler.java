package com.crm.customer.model;

import com.crm.common.Employee;
import com.crm.communication.providers.CommunicationChannelProvider;

public interface ComplaintEscalationHandler {
    void onSlaBreached(Complaint complaint, Customer customer, Employee supervisor, CommunicationChannelProvider factory);
}
