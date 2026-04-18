package com.crm.communication.controller;

import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.factory.CommunicationFactory;
<<<<<<< Updated upstream
=======
import com.crm.common.Employee;
import com.crm.customer.model.Customer;
import com.crm.customer.model.Message;
import com.crm.communication.Notification;
>>>>>>> Stashed changes

public class CommunicationController {
    public CommunicationChannel selectChannel(CommunicationFactory factory) {
        return factory.createChannel();
    }
<<<<<<< Updated upstream
=======

    public void sendMessage(CommunicationFactory factory, String to, String messageText) {
        CommunicationChannel channel = selectChannel(factory);
        channel.sendMessage(to, messageText);
    }

    public void sendMessageFromEmployeeToCustomer(Employee employee, Customer customer, String messageText,
            CommunicationFactory factory) {
        System.out.println(
                "\n>>> Interaction: Employee [" + employee.getName() + "] -> Customer [" + customer.getName() + "]");

        Message message = factory.createMessage(messageText);
        Notification notification = factory.createNotification("New message from " + employee.getName());
        CommunicationChannel channel = selectChannel(factory);

        message.prepare();
        channel.sendMessage(customer.getEmail(), messageText);
        notification.send();
    }

    public void sendMessageFromCustomerToEmployee(Customer customer, Employee employee, String messageText,
            CommunicationFactory factory) {
        System.out.println(
                "\n>>> Interaction: Customer [" + customer.getName() + "] -> Employee [" + employee.getName() + "]");

        Message message = factory.createMessage(messageText);
        Notification notification = factory.createNotification("New inquiry from " + customer.getName());
        CommunicationChannel channel = selectChannel(factory);

        message.prepare();
        channel.sendMessage(employee.getEmail(), messageText);
        notification.send();
    }
>>>>>>> Stashed changes
}
