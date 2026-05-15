package com.crm.communication;

import com.crm.communication.channel.CommunicationChannel;

// Bridge Pattern — Refined Abstraction for notifications that use specific channels.
public class ChannelNotification extends Notification {

    public ChannelNotification(String title, CommunicationChannel channel) {
        super(title, channel);
    }

    @Override
    public void send() {
        channel.sendMessage("", "Notification: " + title);
    }
}
