package com.crm.communication;

import com.crm.communication.channel.CommunicationChannel;

// Bridge Pattern — Abstraction for notifications.
public abstract class Notification {
    protected String title;
    protected CommunicationChannel channel;

    public Notification(String title, CommunicationChannel channel) {
        this.title = title;
        this.channel = channel;
    }

    public abstract void send();
}
