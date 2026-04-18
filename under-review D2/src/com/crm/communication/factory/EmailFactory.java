package com.crm.communication.factory;

import com.crm.customer.model.Message;
import com.crm.communication.Notification;
import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.channel.Email.EmailChannel;
import com.crm.communication.channel.Email.EmailMessage;
import com.crm.communication.ChannelNotification;

public class EmailFactory implements CommunicationFactory {
    @Override
    public CommunicationChannel createChannel() {
        return new EmailChannel();
    }

    @Override
    public Message createMessage(String content) {
        return new EmailMessage(content);
    }

    @Override
    public Notification createNotification(String title) {
        return new ChannelNotification(title, new EmailChannel());
    }
}
