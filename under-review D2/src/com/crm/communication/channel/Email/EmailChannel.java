package com.crm.communication.channel.Email;

import com.crm.communication.channel.CommunicationChannel;

// Bridge Pattern — Concrete Implementor for Email communication.
public class EmailChannel implements CommunicationChannel {
    @Override
    public void sendMessage(String to, String message) {
        // Skeleton: no real email integration
        System.out.println("Email to " + to + ": " + message);
    }
}
