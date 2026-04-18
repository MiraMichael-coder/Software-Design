package com.crm.communication.factory;

import com.crm.customer.model.Message;
import com.crm.communication.Notification;
import com.crm.communication.channel.CommunicationChannel;

public interface CommunicationFactory {
    CommunicationChannel createChannel();

    Message createMessage(String content);

    Notification createNotification(String title);
}
