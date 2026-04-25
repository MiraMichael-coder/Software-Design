package com.crm.communication.providers;

import com.crm.customer.model.Message;
import com.crm.communication.Notification;
import com.crm.communication.channel.CommunicationChannel;

public interface CommunicationChannelProvider {
    CommunicationChannel createChannel();

    Message createMessage(String content);

    Notification createNotification(String title);
}
