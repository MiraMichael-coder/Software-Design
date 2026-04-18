package com.crm.communication.factory;

import com.crm.customer.model.Message;
import com.crm.communication.Notification;
import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.channel.SMS.SMSChannel;
import com.crm.communication.channel.SMS.SmsMessage;
import com.crm.communication.ChannelNotification;

public class SMSFactory implements CommunicationFactory {
    @Override
    public CommunicationChannel createChannel() {
        return new SMSChannel();
    }

    @Override
    public Message createMessage(String content) {
        return new SmsMessage(content);
    }

    @Override
    public Notification createNotification(String title) {
        return new ChannelNotification(title, new SMSChannel());
    }
}
