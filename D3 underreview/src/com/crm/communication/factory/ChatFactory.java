package com.crm.communication.factory;

import com.crm.customer.model.Message;
import com.crm.communication.Notification;
import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.channel.Chat.ChatChannel;
import com.crm.communication.channel.Chat.ChatMessage;
import com.crm.communication.ChannelNotification;

public class ChatFactory implements CommunicationFactory {
    @Override
    public CommunicationChannel createChannel() {
        return new ChatChannel();
    }

    @Override
    public Message createMessage(String content) {
        return new ChatMessage(content);
    }

    @Override
    public Notification createNotification(String title) {
        return new ChannelNotification(title, new ChatChannel());
    }
}
